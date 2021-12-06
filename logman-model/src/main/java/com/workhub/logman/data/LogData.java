package com.workhub.logman.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.workhub.commons.utils.json.UtilJson;
import com.workhub.commons.utils.json.deserialization.LocalDateTimeDeserializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Log data transfer class
 *
 * @author alexkirillov
 * @since 0.0.1 | 23.11.2021
 */

@Data
public class LogData implements Serializable, Comparable<LogData> {
    private UUID logId;

    /* LogMan specific data */

    /* Time of the log insertion into the db */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime insertStamp;
    /* LogMan host name */
    private String lmHost;
    /* LogMan url address */
    private String lmAddress;

    /* Incoming log data */

    /* Name of the logger that produced the log */
    private String logger;
    /* Name of the subsystem that produced the log */
    private String subsystem;
    /* Subsystems host name */
    private String subHost;
    /* Subsystems url address */
    private String subAddress;
    /* Time the log was produced on the subsystem */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createStamp;

    private String ex;
    private String message;

    @Override
    public String toString() {
        return
                "{" +
                "\"logId\":\"" + logId + '\"' +
                ", \"insertStamp\":\"" + insertStamp + '\"' +
                ", \"lmHost\":\"" + lmHost + '\"' +
                ", \"lmAddress\":\"" + lmAddress + '\"' +
                ", \"logger\":\"" + logger + '\"' +
                ", \"subsystem\":\"" + subsystem + '\"' +
                ", \"subHost\":\"" + subHost + '\"' +
                ", \"subAddress\":\"" + subAddress + '\"' +
                ", \"createStamp\":\"" + createStamp + '\"' +
                ", \"ex\":\"" + ex + '\"' +
                ", \"message\":\"" + message + '\"' +
                '}';
    }

    public String toJsonString() {
        return UtilJson.generateJsonObjectStringForClass(this);
    }

    @Override
    public int compareTo(LogData other) {
        LocalDateTime thisTime = this.getCreateStamp();
        LocalDateTime otherTime = other.getCreateStamp();
        return thisTime.compareTo(otherTime);
    }

}
