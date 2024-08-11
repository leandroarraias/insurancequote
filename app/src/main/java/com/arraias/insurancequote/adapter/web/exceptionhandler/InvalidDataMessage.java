package com.arraias.insurancequote.adapter.web.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidDataMessage {
    private String message;
    private List<String> invalidData;
    private String spanId;
    private String traceId;
}
