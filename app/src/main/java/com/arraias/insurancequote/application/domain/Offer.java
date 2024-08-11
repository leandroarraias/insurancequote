package com.arraias.insurancequote.application.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
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
public class Offer {

    private UUID id;

    @JsonAlias("product_id")
    private UUID productId;

    private String name;

    @JsonAlias("created_at")
    private LocalDateTime createdAt;

    private boolean active;
    List<Coverage> coverages;
    List<AssistanceType> assistances;

    @JsonAlias("monthly_premium_amount")
    private MonthlyPremiumAmount MonthlyPremiumAmount;

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

    public Coverage getCoverageDetails(CoverageType type) {

        for (Coverage coverage : getCoverages()) {
            if (coverage.getType().equals(type)) {
                return coverage;
            }
        }

        return null;

    }
}
