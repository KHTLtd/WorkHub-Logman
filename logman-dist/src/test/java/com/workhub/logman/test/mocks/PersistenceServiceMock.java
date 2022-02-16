package com.workhub.logman.test.mocks;

import com.workhub.logman.data.LogData;
import com.workhub.logman.data.LogDataSearchParams;
import com.workhub.logman.exceptions.PersistenceServiceException;
import com.workhub.logman.persistence.IPersistenceService;

import java.util.List;

public class PersistenceServiceMock implements IPersistenceService {

    @Override
    public void save(LogData log) {

    }

    @Override
    public void removePartition(String dsKey, int days) {

    }

    @Override
    public List<LogData> findByTraceId(LogDataSearchParams params) {
        return null;
    }
}
