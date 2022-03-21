package com.workhub.logman.dao.mapping;

import com.workhub.logman.data.LogData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class LogDataNamedParameterMapper implements RowMapper<LogData> {
    @Override
    public LogData mapRow(ResultSet rs, int rowNum) throws SQLException {
        LogData logData = new LogData();
        logData.setLogId(UUID.fromString(rs.getString("log_id")));
        logData.setLmHost(rs.getString("lm_host"));
        logData.setLmAddress(rs.getString("lm_address"));
        logData.setDistrSubsystem(rs.getString("subsystem"));
        logData.setSubHost(rs.getString("sub_host"));
        logData.setSubAddress(rs.getString("sub_address"));
        logData.setLogger(rs.getString("logger"));
        logData.setMessage(rs.getString("message"));
        logData.setEx(rs.getString("ex"));
        logData.setStamp(LocalDateTime.ofInstant(rs.getTimestamp("create_stamp").toInstant(), ZoneId.systemDefault()));
        logData.setInsertStamp(LocalDateTime.ofInstant(rs.getTimestamp("insert_stamp").toInstant(), ZoneId.systemDefault()));
        return logData;
    }
}
