package com.workhub.logman.dao.mapping;

import com.workhub.logman.data.LogData;
import com.workhub.logman.data.constants.SchemaName;
import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

/**
 * Mapping class for DB insertion with pgBulkInsert
 *
 * @author alexkirillov
 * @since 1.0.0 | 24.11.2021
 */
public class LogDataPgBulkMapping extends AbstractMapping<LogData> {

    public LogDataPgBulkMapping(SchemaName schema) {
        super(schema.toString(), "log");

        //Strings
        mapText("lm_host", LogData::getLmHost);
        mapText("lm_address", LogData::getLmAddress);
        mapText("logger", LogData::getLogger);
        mapText("subsystem", LogData::getDistrSubsystem);
        mapText("sub_host", LogData::getSubHost);
        mapText("sub_address", LogData::getSubAddress);
        mapText("ex", LogData::getEx);
        mapText("message", LogData::getMessage);
        //LocalDateTime
        mapTimeStamp("insert_stamp", LogData::getInsertStamp);
        mapTimeStamp("create_stamp", LogData::getStamp);
    }

}
