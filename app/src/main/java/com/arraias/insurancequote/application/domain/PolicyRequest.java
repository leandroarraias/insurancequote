package com.arraias.insurancequote.application.domain;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PolicyRequest implements Serializable {
    private UUID quoteId;
}
