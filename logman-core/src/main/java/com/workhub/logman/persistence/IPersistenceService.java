package com.workhub.logman.persistence;

import com.workhub.logman.data.LogData;
import com.workhub.logman.data.LogDataSearchParams;
import com.workhub.logman.exceptions.PersistenceServiceException;
import lombok.extern.java.Log;

import java.util.List;

/**
 * Persistence service interface
 *
 * @author alexkirillov
 * @since 01.000.00 | 24.11.2021
 */
public interface IPersistenceService  {

    void save(LogData log) throws Exception;

    void removePartition(String dsKey, int days);

    List<LogData> findByTraceId(LogDataSearchParams params) throws PersistenceServiceException;
}
