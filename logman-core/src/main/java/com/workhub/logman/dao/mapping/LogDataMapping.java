package com.workhub.logman.dao.mapping;

import com.workhub.logman.data.LogData;
import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

/**
 * Mapping class for DB insertion with pgBulkInsert
 *
 * @author alexkirillov
 * @since 1.0.0 | 24.11.2021
 */
public class LogDataMapping extends AbstractMapping<LogData> {

    protected LogDataMapping(String schemaName, String tableName) {
        super(schemaName, tableName);

        //Strings
        mapText("lmHost", LogData::getLmHost);
        mapText("lmAddress", LogData::getLmAddress);
        mapText("logger", LogData::getLogger);
        mapText("subsystem", LogData::getSubsystem);
        mapText("subHost", LogData::getSubHost);
        mapText("subAddress", LogData::getSubAddress);
        mapText("ex", LogData::getEx);
        mapText("message", LogData::getMessage);
        //LocalDateTime
        mapTimeStamp("insertStamp", LogData::getInsertStamp);
        mapTimeStamp("createStamp", LogData::getCreateStamp);
    }

}
