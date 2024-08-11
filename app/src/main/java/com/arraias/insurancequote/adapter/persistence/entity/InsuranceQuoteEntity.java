package com.arraias.insurancequote.adapter.persistence.entity;

import com.arraias.insurancequote.application.domain.AssistanceType;
import com.arraias.insurancequote.application.domain.CategoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "INSURANCE_QUOTE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceQuoteEntity {

    @Id
    @GeneratedValue
    private UUID id;
    private UUID insurancePolicyId;
    private UUID productId;
    private UUID offerId;
    private String category;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal totalCoverageAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "insuranceQuote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoverageEntity> coverages;

    @OneToMany(mappedBy = "insuranceQuote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssistanceEntity> assistances;

    @OneToOne(mappedBy = "insuranceQuote", cascade = CascadeType.ALL, orphanRemoval = true)
    private CustomerEntity customer;

    public List<CoverageEntity> getCoverages() {

        if (coverages == null) {
            coverages = new ArrayList<>();
        }

        return coverages;
    }

    public List<AssistanceEntity> getAssistances() {

        if (assistances == null) {
            assistances = new ArrayList<>();
        }

        return assistances;
    }
}
