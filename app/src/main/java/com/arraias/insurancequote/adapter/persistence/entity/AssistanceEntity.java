package com.arraias.insurancequote.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "INSURANCE_QUOTE_ASSISTANCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssistanceEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private InsuranceQuoteEntity insuranceQuote;

    private String assistance;
}
