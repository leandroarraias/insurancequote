package com.arraias.insurancequote.application.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MonthlyPremiumAmount {

    @JsonAlias("max_amount")
    private BigDecimal maxAmount;

    @JsonAlias("min_amount")
    private BigDecimal minAmount;

    @JsonAlias("suggested_amount")
    private BigDecimal suggestedAmount;

}
