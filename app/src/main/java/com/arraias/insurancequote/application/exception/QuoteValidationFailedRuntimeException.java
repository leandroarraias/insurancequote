package com.arraias.insurancequote.application.exception;

public class QuoteValidationFailedRuntimeException extends RuntimeException {

    public QuoteValidationFailedRuntimeException(String message) {
        super(message);
    }
}
