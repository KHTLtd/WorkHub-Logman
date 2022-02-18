package com.workhub.logman.handlers.errors;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

@Slf4j
public class KafkaErrorHandler implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException e) {
        log.error("Failed to process message from kafka. headers={}, body={}, exc={}",
                message.getHeaders(), message.getPayload(), e);
        return null;
    }

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException e, Consumer<?, ?> consumer) {
        log.error("Failed to process message from kafka. headers={}, body={}, consumer={}, exc={}",
                message.getHeaders(), message.getPayload(), consumer, e);
        return null;
    }

}
