package com.workhub.logman.kafka.consumer;

import com.workhub.commons.utils.json.UtilJson;
import com.workhub.logman.data.LogData;
import com.workhub.logman.persistence.service.IPersistenceService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaLogsConsumer {

    @Autowired
    private IPersistenceService service;

    @KafkaListener(topicPattern = "workhub.*.logman.log", concurrency = "1")
    public void save(ConsumerRecord<String, String> logData) throws Exception {
        LogData logRecord = UtilJson.readFromJson(logData.value(), LogData.class);
        service.save(logRecord);
    }

}
