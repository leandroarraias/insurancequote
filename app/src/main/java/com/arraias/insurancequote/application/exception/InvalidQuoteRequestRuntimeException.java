package com.arraias.insurancequote.application.exception;

public class InvalidQuoteRequestRuntimeException extends RuntimeException {

    public InvalidQuoteRequestRuntimeException(String message) {
        super(message);
    }
}
