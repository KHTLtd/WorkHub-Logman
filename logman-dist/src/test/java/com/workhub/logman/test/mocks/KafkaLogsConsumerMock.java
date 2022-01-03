package com.workhub.logman.test.mocks;

import com.workhub.logman.kafka.consumer.KafkaLogsConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class KafkaLogsConsumerMock extends KafkaLogsConsumer {
    @Override
    public void save(ConsumerRecord<String, String> logData) throws Exception {
    }
}
