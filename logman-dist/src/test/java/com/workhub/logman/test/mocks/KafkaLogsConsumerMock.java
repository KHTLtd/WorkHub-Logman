package com.workhub.logman.test.mocks;

import com.workhub.logman.kafka.KafkaLogsConsumer;
import com.workhub.logman.persistence.IPersistenceService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class KafkaLogsConsumerMock extends KafkaLogsConsumer {

    public KafkaLogsConsumerMock(IPersistenceService service) {
        super(service);
    }

    @Override
    public void save(ConsumerRecord<String, String> logData) {
    }
}
