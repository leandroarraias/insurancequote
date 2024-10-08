package com.arraias.insurancequote.adapter.web.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    private String message;
    private String spanId;
    private String traceId;
}
