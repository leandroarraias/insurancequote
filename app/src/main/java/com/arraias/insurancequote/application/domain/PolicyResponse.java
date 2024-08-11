package com.arraias.insurancequote.application.domain;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PolicyResponse {
    private UUID id;
    private UUID quoteId;
}
