package com.workhub.logman.dao.impl;

import com.workhub.logman.dao.ILogDataDao;
import com.workhub.logman.dao.mapping.LogDataNamedParameterMapper;
import com.workhub.logman.dao.mapping.LogDataPgBulkMapping;
import com.workhub.logman.data.LogDataSearchParams;
import com.workhub.logman.data.constants.SchemaName;
import com.workhub.logman.exceptions.LogmanDatasourceException;
import com.workhub.logman.exceptions.PersistenceServiceException;
import com.workhub.logman.routing.RoutingDataSource;
import com.workhub.logman.data.LogData;
import de.bytefish.pgbulkinsert.PgBulkInsert;
import de.bytefish.pgbulkinsert.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO implementation for db operations
 *
 * @since 1.0.0 | 03.12.2021
 * @author alexkirillov
 */
@Slf4j
@Repository
@Transactional(rollbackFor = Exception.class)
public class LogDataDaoImpl implements ILogDataDao {

    private final String  FIND_BY_LOG_ID_QUERY = "select * from " + SchemaName.LOG + ".log ld "
            + "where ld.create_stamp between :from and :to "
            + "and ld.log_id = :logId "
            + "order by ld.create_stamp desc";

    @Autowired
    @Qualifier("logmanDs")
    private RoutingDataSource dataSource;

    @Autowired
    private NamedParameterJdbcOperations namedParameterTemplate;

    @Override
    public void saveLogs(List<LogData> logs) throws SQLException {
        if (CollectionUtils.isEmpty(logs)) {
            return;
        }
        final Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            log.debug("Obtained connection: {}, of class: {}, datasource: {}",
                    connection, connection.getClass(), this.dataSource);
            PgBulkInsert<LogData> bulkInsert = new PgBulkInsert<>(new LogDataPgBulkMapping(SchemaName.LOG));
            final PGConnection pgConnection = connection.unwrap(PGConnection.class);
            bulkInsert.saveAll(pgConnection, logs.stream());
        } catch (Exception e) {
            log.error("Failed to save data to Database. Reason: " + e.getMessage(), e);
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(connection, this.dataSource);
        }
        log.debug("Successfully inserted {} entries", logs.size());
    }

    @Override
    public void removePartitions(int days, String dsKey) throws LogmanDatasourceException, PersistenceServiceException {
        DataSource ds;
        if (!StringUtils.isNullOrWhiteSpace(dsKey)) {
            log.debug("Received a DB key to remove partitions. Key: {}", dsKey);
            ds = dataSource.getInitialisedDataSource(dsKey);
        } else {
            log.debug("No Key for partitions removal was received. Removing from current in-use DB.");
            ds = dataSource;
        }
        JdbcOperations template = new JdbcTemplate(ds);
        List<String> partitionsToRemove = new ArrayList<>();
        try {
            partitionsToRemove = getPartitionsToRemove(days, template);
        } catch (PersistenceServiceException e) {
            log.error("Caught exception while seraching for partitions to remove: "
                    + e.getMessage(), e);
            throw e;
        }
        for (String partition : partitionsToRemove) {
            try {
                this.removeSinglePartition(partition, template);
            } catch (PersistenceServiceException e) {
                log.error("Caught exception while removing partitions: "
                        + e.getMessage(), e);
            }
        }
    }


    @Override
    public List<LogData> findByTraceId(LogDataSearchParams searchParams) throws PersistenceServiceException {
        List<LogData> logDataList;
        try {
            MapSqlParameterSource map = new MapSqlParameterSource();
            map.addValue("logId", searchParams.getLogId());
            map.addValue("from", searchParams.getStartTime());
            map.addValue("to", searchParams.getEndTime());

            logDataList = namedParameterTemplate.query(FIND_BY_LOG_ID_QUERY, map, new LogDataNamedParameterMapper());
        } catch (Exception e) {
            log.error("Failed to retrieve logs by traceId: " + e.getMessage(), e);
            throw new PersistenceServiceException("Failed to get logs: " + e.getMessage(), e);
        }
        return logDataList;
    }


    /**
     * Look for partitions that fit the 'days' creteria
     * and return a list of them.
     *
     * @param days number of day before partition needs to be deleted
     * @param template jdbcTemplate
     * @return partitionNamesList
     * @throws PersistenceServiceException
     */
    private List<String> getPartitionsToRemove(int days, JdbcOperations template) throws PersistenceServiceException {
        log.debug("Looking for partitions older than {} days", days);
        List<String> partitionsToDrop = new ArrayList<>();
        try {
            partitionsToDrop = template.query("select partition from " + SchemaName.PG_PATH
                + "pathman-partition_list where range_max::date < now() - interval '" + days + " day'",
                (resultSet, i) -> resultSet.getString(1));
        } catch (Exception e) {
            throw new PersistenceServiceException("Failed to retrieve partitions to remove: " + e.getMessage(), e);
        }
        log.debug("Found {} partitions", partitionsToDrop.size());
        return partitionsToDrop;
    }

    /**
     * Remove a single partition from the db
     * that fits the 'partitionName' param
     *
     * @param partitionName name of the partition to delete
     * @param template jdbcTemplate
     * @throws PersistenceServiceException
     */
    private void removeSinglePartition(String partitionName, JdbcOperations template) throws PersistenceServiceException {
        log.debug("Trying to delete a partition {}", partitionName);
        try {
            template.execute("drop table " + partitionName + " cascade;");
        } catch (Exception e) {
            throw new PersistenceServiceException("Failed to delete partition: " + partitionName, e);
        }
        log.debug("Partition {} was removed", partitionName);
    }

}
