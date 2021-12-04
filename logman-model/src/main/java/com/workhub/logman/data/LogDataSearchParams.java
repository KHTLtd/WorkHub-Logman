package com.workhub.logman.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogDataSearchParams {
    private String logId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
