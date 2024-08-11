package com.arraias.insurancequote.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "INSURANCE_QUOTE_CUSTOMER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private InsuranceQuoteEntity insuranceQuote;

    private String documentNumber;
    private String name;
    private String type;
    private String gender;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
}
