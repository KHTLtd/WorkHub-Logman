package com.workhub.logman.kafka.consumer;

import com.workhub.logman.data.LogData;
import json.UtilJson;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class KafkaLogsConsumer {
    List<LogData> vlist = new ArrayList<>();
    List<String> klist = new ArrayList<>();

    @KafkaListener(topicPattern = "workhub.*.logman.log", concurrency = "1")
    public void save(ConsumerRecord<String, String> logData){
        LogData logRecord = UtilJson.readFromJson(logData.value(), LogData.class);
        klist.add(logData.key());
        vlist.add(logRecord);
    }

}
