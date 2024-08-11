package com.arraias.insurancequote.application.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    private UUID id;

    private String name;

    @JsonAlias("created_at")
    private LocalDateTime createdAt;

    private boolean active;

    private List<UUID> offers;

    public List<UUID> getOffers() {

        if (offers == null) {
            offers = new ArrayList<>();
        }

        return offers;

    }
}
