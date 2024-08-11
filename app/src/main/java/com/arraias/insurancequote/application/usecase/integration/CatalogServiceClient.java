package com.arraias.insurancequote.application.usecase.integration;

import com.arraias.insurancequote.application.domain.Offer;
import com.arraias.insurancequote.application.domain.Product;

import java.util.UUID;

public interface CatalogServiceClient {
    Offer getOffer(UUID offerId);
    Product getProduct(UUID productId);
}
