package spring.config.kafka;

import com.workhub.logman.data.LogData;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    @Bean(name = "kafkaLogTemplate")
    KafkaTemplate<String, String> kafkaLogTemplate(ProducerFactory<String, String> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Bean(name = "kafkaLogProducer")
    ProducerFactory<String, String> kafkaLogProducer(KafkaProperties properties) {
        return new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
    }

    @Bean(name = "kafkaLogConsumer")
    ConsumerFactory<String, LogData> kafkaLogConsumer(KafkaProperties properties) {
        return new DefaultKafkaConsumerFactory<>(properties.buildConsumerProperties());
    }

}
