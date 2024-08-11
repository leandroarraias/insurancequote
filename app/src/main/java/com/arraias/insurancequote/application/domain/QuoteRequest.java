package com.arraias.insurancequote.application.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuoteRequest {

    @NotNull
    private UUID productId;

    @NotNull
    private UUID offerId;

    @NotNull
    private CategoryType category;

    @NotNull
    private BigDecimal totalMonthlyPremiumAmount;

    @NotNull
    private BigDecimal totalCoverageAmount;

    @NotEmpty @Valid
    private List<Coverage> coverages;

    @NotEmpty @Valid
    private List<AssistanceType> assistances;

    @NotNull @Valid
    private Customer customer;

    public List<Coverage> getCoverages() {

        if (coverages == null) {
            coverages = new ArrayList<>();
        }

        return coverages;
    }

    public List<AssistanceType> getAssistances() {

        if (assistances == null) {
            assistances = new ArrayList<>();
        }

        return assistances;
    }
}
