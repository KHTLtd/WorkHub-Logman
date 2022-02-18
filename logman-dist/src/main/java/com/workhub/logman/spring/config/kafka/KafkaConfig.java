package com.workhub.logman.spring.config.kafka;

import com.workhub.commons.kafka.consumer.config.KafkaCommonsConsumerBeanConfig;
import com.workhub.commons.kafka.producer.config.KafkaCommonsProducerBeanConfig;
import com.workhub.logman.handlers.errors.KafkaErrorHandler;
import com.workhub.logman.kafka.KafkaLogsConsumer;
import com.workhub.logman.persistence.IPersistenceService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ProducerFactory;

@Import({KafkaCommonsProducerBeanConfig.class, KafkaCommonsConsumerBeanConfig.class})
public class KafkaConfig {

    @Bean
    KafkaLogsConsumer kafkaLogsConsumer(IPersistenceService service) {
        return new KafkaLogsConsumer(service);
    }

    @Bean("kafkaListenerErrorHandler")
    KafkaErrorHandler errorHandler() {
        return new KafkaErrorHandler();
    }

    @Bean
    ProducerFactory<Object, Object> producerFactory(ApplicationContext context) {
        return (ProducerFactory<Object, Object>) context.getBean("whKafkaProducerFactory");
    }

}
