package com.workhub.logman.persistence.impl;

import com.workhub.commons.utils.inet.InetAddressUtil;
import com.workhub.logman.dao.ILogDataDao;
import com.workhub.logman.data.LogData;
import com.workhub.logman.data.LogDataSearchParams;
import com.workhub.logman.exceptions.PersistenceServiceException;
import com.workhub.logman.handlers.logs.IUnsavedLogsHandler;
import com.workhub.logman.handlers.logs.impl.UnsavedLogsHandler;
import com.workhub.logman.persistence.IPersistenceService;
import de.bytefish.pgbulkinsert.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Persistence service impl for multithreaded db operations
 *
 * @since 1.0.0 | 03.12.2021
 * @author alexkirillov
 */
@Slf4j
public class PersistenceServiceImpl implements IPersistenceService, DisposableBean {
    private final long DESTROY_TIME_OUT = 15 * 60 * 1000; // 15 minutes
    private final long BACKOFF_TIME = 500L;
    private final String EMPTY_ENTRY = "donut";

    private volatile long lastUpdateTime = -1L;

    private final int batchSize = 2; //FIXME: just for initial
    private final long batchTime = 10 * 1000; // 10 sec //FIXME: just for initial

    private final AsyncTaskExecutor persistenceTaskExecutor;
    private final ConcurrentLinkedQueue<LogData> queue = new ConcurrentLinkedQueue<>();
    private final Object queueMonitorLock = new Object();

    @Autowired
    private ILogDataDao dao;

    private IUnsavedLogsHandler unsavedLogsHandler = new UnsavedLogsHandler();

    public PersistenceServiceImpl(AsyncTaskExecutor persistenceTaskExecutor, ILogDataDao dao) {
        this.persistenceTaskExecutor = persistenceTaskExecutor;
        this.dao = dao;
    }

    @Override
    public void save(LogData logData) {
        // Fill in the empty fields that are indexed
        logData.setLogId(UUID.randomUUID());
        logData.setInsertStamp(LocalDateTime.now());
        if (StringUtils.isNullOrWhiteSpace(logData.getSubAddress())) {
            logData.setSubAddress(EMPTY_ENTRY);
        }
        if (StringUtils.isNullOrWhiteSpace(logData.getSubsystem())) {
            logData.setSubsystem(EMPTY_ENTRY);
        }
        if (StringUtils.isNullOrWhiteSpace(logData.getSubHost())) {
            logData.setSubHost(EMPTY_ENTRY);
        }
        logData.setLmHost(InetAddressUtil.getHostOrIp());

        queue.add(logData);

        touch();
    }

    @Override
    public void removePartition(String dsKey, int days) {
        if (days == 0) {
            log.info("Passe Partition-Lifetime criteria is not configured or equals 0, "
                    + "partitions will not be removed");
            return;
        }
        try {
            dao.removePartitions(days, dsKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public List<LogData> findByTraceId(LogDataSearchParams params) throws PersistenceServiceException {
        List<LogData> logDataList = new ArrayList<>();
        try {
            logDataList = dao.findByTraceId(params);
        } catch (Exception e) {
            throw new PersistenceServiceException("Error while getting log data ", e);
        }
        return logDataList;
    }

    private void touch() {
        int curQueueSize = queue.size();

        if (curQueueSize < 1) {
            log.debug("No activity detected");
            return;
        }
        if (curQueueSize < batchSize && ((System.currentTimeMillis() - lastUpdateTime) < batchTime)) {
            log.debug("No begin-to-write-in-db event has occurred");
            return;
        } else {
            try {
                persistenceTaskExecutor.execute(this::persistQueue);
                log.debug("Started the process of saving log data in the DB");
            } catch (Exception e) {
                log.error("The process of saving loog data have already been started.");
            }
        }
    }

    private void persistQueue() {
        log.info("Starting saving collected logs, current number: {}", queue.size());
        List<LogData> logList = new ArrayList<>(batchSize);
        LogData logData;
        int count = 0;
        synchronized (queueMonitorLock) {
            while ((logData = queue.poll()) != null && count < batchSize) {
                logList.add(logData);
                count++;
            }
        }
        try {
            dao.saveLogs(logList);
        } catch (SQLException e) {
            log.error("Failed to save logs in DB, saving them in local files", e);
            unsavedLogsHandler.writeUnsavedLogs(logList);
        }

        lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public void destroy() throws Exception {
        long time = System.currentTimeMillis();
        boolean isQueueEmpty = queue.isEmpty();
        while (!isQueueEmpty) {
            if ((System.currentTimeMillis() - time) > DESTROY_TIME_OUT) {
                throw new PersistenceServiceException("Unable to finish saving all the logs before the application is terminated.");
            }
            touch();
            applyBackOff();
            isQueueEmpty = queue.isEmpty();
        }
    }

    private void applyBackOff() {
        log.warn("Back-Off was triggered");
        try {
            Thread.sleep(BACKOFF_TIME);
        } catch (Exception e) {
            log.error("Failed to apply Back-Off", e);
        }
    }
}
