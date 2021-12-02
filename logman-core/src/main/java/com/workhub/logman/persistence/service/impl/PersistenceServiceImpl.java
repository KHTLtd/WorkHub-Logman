package com.workhub.logman.persistence.service.impl;

import com.workhub.logman.dao.ILogDataDao;
import com.workhub.logman.data.LogData;
import com.workhub.logman.persistence.service.IPersistenceService;
import lombok.extern.java.Log;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Persistence service impl for db operations
 *
 * @since 1.0.0 | 03.12.2021
 * @author alexkirillov
 */
public class PersistenceServiceImpl implements IPersistenceService {

    private final int batchSize;

    private final Queue<LogData> queue;

    @Autowired
    ILogDataDao dao;

    public PersistenceServiceImpl() {
        queue = new PriorityQueue<>();
        batchSize = 2;
    }

    @Override
    public void saveLog(LogData log) throws Exception {
        queue.add(log);
        if (queue.size() < batchSize) {
            return;
        }
        flush();
    }

    @Override
    public void removePartition() {

    }

    @Override
    public List<Object> getPartitionToRemove() {
        return null;
    }

    @Override
    public Object getLogList() {
        return null;
    }

    private void flush() throws Exception {
        List<LogData> list = new ArrayList<>(queue);
        dao.saveLogs(list);
    }
}
