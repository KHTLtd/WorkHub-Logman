package com.workhub.logman.web.controller;

import com.workhub.logman.data.LogData;
import com.workhub.logman.data.LogDataSearchParams;
import com.workhub.logman.kafka.KafkaLogsConsumer;
import com.workhub.logman.persistence.IPersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class LogController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaLogsConsumer consumer;
    @Autowired
    private IPersistenceService service;

    private final String TOPIC = "workhub.dev.test.logman.log";


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

    //TODO FOR TESTING
    @GetMapping("/postRandom")
    @ResponseBody
    public String produceRandomKafkaMsg() {
        kafkaTemplate.send(TOPIC, getPlaceholderData().toJsonString());
        System.out.println("posted");
        return "OK";
    }

    //TODO FOR TESTING
    public LogData getPlaceholderData() {
        LogData logData = new LogData();
        logData.setCreateStamp(LocalDateTime.now());
        logData.setInsertStamp(LocalDateTime.now().plusMinutes(10));
        logData.setLmAddress("donut");
        logData.setSubAddress("");
        logData.setLmHost("donut");
        logData.setSubHost("");
        logData.setLogger("donut");
        logData.setSubsystem("");
        logData.setMessage("donut");
        logData.setEx("donut");
        logData.setLogId(UUID.randomUUID());
        return logData;
    }

}
