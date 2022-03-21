package com.workhub.logman.kafka;

import com.workhub.commons.utils.json.UtilJson;
import com.workhub.logman.data.LogData;
import com.workhub.logman.exceptions.KafkaParseMessageException;
import com.workhub.logman.persistence.IPersistenceService;
import com.workhub.logman.util.Converter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@AllArgsConstructor
public class KafkaLogsConsumer {

    private IPersistenceService service;

    @KafkaListener(topics = "workhub.dev.test.logman.log", concurrency = "3", errorHandler = "kafkaListenerErrorHandler")
    public void save(ConsumerRecord<String, String> data) {
        log.info("Received log entry to write from topic: {}", data.topic());
        LogData logData = UtilJson.readFromJson(data.value(), LogData.class);
        Converter.trimDataForInsertion(logData);

        service.save(logData);
    }

}
