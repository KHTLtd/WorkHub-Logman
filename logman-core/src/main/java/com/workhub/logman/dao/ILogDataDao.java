package com.workhub.logman.dao;

import com.workhub.logman.data.LogData;

import java.sql.SQLException;
import java.util.List;

/**
 * Database operations interface
 *
 * @author alexkirillov
 * @since 1.0.0 | 24.11.2021
 */
public interface ILogDataDao {

    /**
     * Insert incoming logs into the DB
     *
     * @param logList list of incoming logs to be inserted in db
     */
    void saveLogs(List<LogData> logList) throws SQLException;

    /**
     * Remove partitions from a certain DB
     *
     * @param days number of days after which partition will be removed
     * @param dsKey key to the DataSource to delete from
     */
    void removePartitions(int days, String dsKey) throws Exception;

    // TODO: Later. For data collecting
    List<LogData> findByTraceId();

}
