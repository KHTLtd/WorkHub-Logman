package com.workhub.logman.exceptions;

public class KafkaParseMessageException extends Exception {
    public KafkaParseMessageException(String message) {
        super(message);
    }

    public KafkaParseMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaParseMessageException(Throwable cause) {
        super(cause);
    }

    public KafkaParseMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
