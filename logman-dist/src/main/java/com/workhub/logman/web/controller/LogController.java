package com.workhub.logman.web.controller;

import com.workhub.logman.data.LogData;
import com.workhub.logman.data.LogDataSearchParams;
import com.workhub.logman.persistence.IPersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class LogController {
    @Autowired
    private IPersistenceService service;

    @PostMapping("/getLogsById")
    @ResponseBody
    public List<LogData> getLists(@RequestBody LogDataSearchParams params) {
        log.info("Querying logs by following parameters: {}", params);
        try {
            return service.findByTraceId(params);
        } catch (Exception e) {
            log.error("Failed to query log records", e);
            return null;
        }
    }

}
