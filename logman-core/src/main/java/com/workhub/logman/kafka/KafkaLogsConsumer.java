package com.workhub.logman.kafka;

import com.workhub.commons.utils.json.UtilJson;
import com.workhub.logman.data.LogData;
import com.workhub.logman.exceptions.KafkaParseMessageException;
import com.workhub.logman.persistence.IPersistenceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@AllArgsConstructor
public class KafkaLogsConsumer {

    private IPersistenceService service;

    @KafkaListener(topics = "workhub.dev.test.log", concurrency = "3", errorHandler = "kafkaListenerErrorHandler")
    public void save(ConsumerRecord<String, String> logData) throws KafkaParseMessageException {
        log.info("Received log entry to write from topic: {}", logData.topic());
        LogData logRecord = UtilJson.readFromJson(logData.value(), LogData.class);
        if (logRecord == null) {
            log.error("Failed to parse incoming message. body: {}. ", logData.value());
            throw new KafkaParseMessageException("Failed to parse incoming message into LogData object");
        }
        service.save(logRecord);
    }

}
