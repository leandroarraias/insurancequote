package com.arraias.insurancequote.application.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InvalidOfferRuntimeException extends RuntimeException {

    private final UUID offerId;

    public InvalidOfferRuntimeException(UUID offerId, String message, Throwable cause) {
        super(message, cause);
        this.offerId = offerId;
    }
}
