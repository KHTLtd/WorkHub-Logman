package com.workhub.logman.web.controller;

import com.workhub.logman.data.LogData;
import com.workhub.logman.kafka.consumer.KafkaLogsConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class LogController {
    @Autowired
    @Qualifier("kafkaLogTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaLogsConsumer consumer;

    final String topic = "workhub.dev.test.logman.log";


    @GetMapping("/postRandom")
    @ResponseBody
    public String produceRandomKafkaMsg() {
        kafkaTemplate.send(topic, getPlaceholderData().toJsonString());
        System.out.println("posted");
        return "OK";
    }

    @GetMapping("/info")
    @ResponseBody
    public Object[] getLists() {
        String keys = consumer.getKlist().toString();
        List<LogData> vals = consumer.getVlist();

        return new Object[]{keys, vals};
    }

    public LogData getPlaceholderData() {
        LogData logData = new LogData();
        logData.setCreateStamp(LocalDateTime.now());
        logData.setInsertStamp(LocalDateTime.now().plusMinutes(10));
        logData.setLmAddress("donut");
        logData.setSubAddress("donut");
        logData.setLmHost("donut");
        logData.setSubHost("donut");
        logData.setLogger("donut");
        logData.setSubsystem("donut");
        logData.setMessage("donut");
        logData.setEx("donut");
        logData.setLogId(UUID.randomUUID());
        return logData;
    }

}
