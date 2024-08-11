package com.arraias.insurancequote.application.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {

    @NotEmpty
    private String documentNumber;

    @NotEmpty
    private String name;

    @NotEmpty
    private String type;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String dateOfBirth;

    @NotEmpty
    private String email;

    @NotEmpty
    private String phoneNumber;

}
