package com.workhub.logman.kafka;

import com.workhub.commons.utils.json.UtilJson;
import com.workhub.logman.data.LogData;
import com.workhub.logman.persistence.IPersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class KafkaLogsConsumer {

    @Autowired
    private IPersistenceService service;

    @KafkaListener(topicPattern = "workhub.*.logman.log", concurrency = "3")
    public void save(ConsumerRecord<String, String> logData) throws Exception {
        log.info("Received log entry to write from topic: {}", logData.topic());
        LogData logRecord = UtilJson.readFromJson(logData.value(), LogData.class);
        service.save(logRecord);
    }

}
