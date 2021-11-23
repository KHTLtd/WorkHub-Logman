package com.workhub.logman;

import com.workhub.logman.data.LogData;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

public class KafkaLogsConsumer extends KafkaConsumer<String, LogData> {

    public KafkaLogsConsumer(Properties properties) {
        super(properties);
    }

}
