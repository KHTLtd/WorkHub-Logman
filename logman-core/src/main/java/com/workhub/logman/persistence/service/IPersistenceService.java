package com.workhub.logman.persistence.service;

import java.util.List;

public interface IPersistenceService  {

    void saveLog();

    void removePartition();

    List<Object> getPartitionToRemove();

    Object getLogList();
}
