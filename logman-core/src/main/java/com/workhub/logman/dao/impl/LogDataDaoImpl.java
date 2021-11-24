package com.workhub.logman.dao.impl;

import com.workhub.logman.dao.ILogDataDao;
import com.workhub.logman.routing.RoutingDataSource;
import com.workhub.logman.data.LogData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LogDataDaoImpl implements ILogDataDao {

    @Autowired
    private RoutingDataSource dataSource;

    @Override
    public void saveLogs(List<LogData> logs) {

    }

    @Override
    public void getPartitionsToRemove(int days) {

    }

    @Override
    public void removePartitions(int days, String dsKey) {

    }

    @Override
    public List<LogData> findByTraceId() {
        return null;
    }

}
