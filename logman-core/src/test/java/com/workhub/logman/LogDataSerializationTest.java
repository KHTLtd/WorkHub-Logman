package com.workhub.logman;

import com.workhub.commons.utils.json.UtilJson;
import com.workhub.logman.data.LogData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class LogDataSerializationTest {

    String incomingLogDataStr = "{\"message\":\"TestMessage for LogData 1123\","
            + "\"logger\":\"TestLogger\",\"logId\":\"8a40dc66-5292-4437-a03d-acd0c41b0c9b\","
            + "\"lmHost\":\"0.0.0.0\",\"lmAddress\":\"localhost\",\"subsystem\":\"TESTING\","
            + "\"subHost\":\"1.1.1.1\",\"subAddress\":\"otherhost\",\"ex\":\"TESTINGException\","
            + "\"insertStamp\":\"1638816178749\",\"createStamp\":\"1638815578749\"}";

    @Test
    public void testDeSerialization() {
        LogData deSerialized = UtilJson.readFromJson(incomingLogDataStr, LogData.class);
        LogData testData = getTestLogDataObject();
        Assertions.assertNotNull(deSerialized);
        Assertions.assertEquals(testData, deSerialized);
    }

    private LogData getTestLogDataObject() {
        LogData logData = new LogData();
        logData.setLogId(UUID.fromString("8a40dc66-5292-4437-a03d-acd0c41b0c9b"));
        logData.setLmHost("0.0.0.0");
        logData.setSubHost("1.1.1.1");
        logData.setLmAddress("localhost");
        logData.setSubAddress("otherhost");
        logData.setSubsystem("TESTING");
        logData.setLogger("TestLogger");
        logData.setMessage("TestMessage for LogData 1123");
        logData.setEx("TESTINGException");
        logData.setCreateStamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(1638815578749L), ZoneId.systemDefault()));
        logData.setInsertStamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(1638816178749L), ZoneId.systemDefault()));

        return logData;
    }

}
