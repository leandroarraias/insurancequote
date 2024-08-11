package com.arraias.insurancequote.application.exception;

public class ApplicationRuntimeException extends RuntimeException {

    public ApplicationRuntimeException(String message) {
        super(message);
    }

    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
