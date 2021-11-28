package com.workhub.logman.persistence.service;

import com.workhub.logman.data.LogData;

import java.util.List;

public interface IPersistenceService  {

    void saveLog(LogData log) throws Exception;

    void removePartition();

    List<Object> getPartitionToRemove();

    Object getLogList();
}
