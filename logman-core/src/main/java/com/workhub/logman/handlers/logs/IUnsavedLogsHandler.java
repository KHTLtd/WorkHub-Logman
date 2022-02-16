package com.workhub.logman.handlers.logs;

import com.workhub.logman.data.LogData;

import java.util.List;

public interface IUnsavedLogsHandler {

    void writeUnsavedLogs(List<LogData> logList);

}
