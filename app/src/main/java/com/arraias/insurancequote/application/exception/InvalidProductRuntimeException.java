package com.arraias.insurancequote.application.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InvalidProductRuntimeException extends RuntimeException {

    private final UUID productId;

    public InvalidProductRuntimeException(UUID productId, String message) {
        super(message);
        this.productId = productId;
    }

    public InvalidProductRuntimeException(UUID productId, String message, Throwable cause) {
        super(message, cause);
        this.productId = productId;
    }
}
