package com.workhub.logman.exceptions;

public class LogmanDatasourceException extends Exception {

    public LogmanDatasourceException(String message) {
        super(message);
    }

    public LogmanDatasourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogmanDatasourceException(Throwable cause) {
        super(cause);
    }

    public LogmanDatasourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
