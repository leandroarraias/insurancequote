package com.arraias.insurancequote.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "INSURANCE_QUOTE_COVERAGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoverageEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private InsuranceQuoteEntity insuranceQuote;

    private String type;
    private BigDecimal coverageValue;
}
