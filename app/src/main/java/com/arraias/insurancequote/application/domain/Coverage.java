package com.arraias.insurancequote.application.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Coverage {

    @NotNull
    private CoverageType type;

    @NotNull
    private BigDecimal value;
}
