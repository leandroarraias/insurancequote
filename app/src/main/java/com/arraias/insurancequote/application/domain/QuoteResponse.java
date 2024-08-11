package com.arraias.insurancequote.application.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuoteResponse {

    private UUID id;
    private UUID insurancePolicyId;
    private UUID productId;
    private UUID offerId;
    private CategoryType category;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal totalCoverageAmount;
    private List<Coverage> coverages;
    private List<AssistanceType> assistances;
    private Customer customer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
