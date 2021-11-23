package com.workhub.logman.data;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Log data transfer class
 *
 * @author alexkirillov
 * @since 0.0.1 | 23.11.2021
 */

@Data
public class LogData implements Serializable {
    /* LogMan specific data */

    /* Time of the log insertion into the db */
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
    private LocalDateTime createStamp;

    private String ex;
    private String message;

}
