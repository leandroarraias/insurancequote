package com.arraias.insurancequote.application.exception;

public class BusinessRuntimeException extends RuntimeException {

    public BusinessRuntimeException(String message) {
        super(message);
    }

    public BusinessRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
