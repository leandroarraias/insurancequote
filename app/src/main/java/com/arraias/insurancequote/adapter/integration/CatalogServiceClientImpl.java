package com.arraias.insurancequote.adapter.integration;

import com.arraias.insurancequote.application.domain.Offer;
import com.arraias.insurancequote.application.domain.Product;
import com.arraias.insurancequote.application.exception.ApplicationRuntimeException;
import com.arraias.insurancequote.application.exception.BusinessRuntimeException;
import com.arraias.insurancequote.application.exception.InvalidOfferRuntimeException;
import com.arraias.insurancequote.application.exception.InvalidProductRuntimeException;
import com.arraias.insurancequote.application.usecase.integration.CatalogServiceClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class CatalogServiceClientImpl implements CatalogServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wiremock.catalogservice.url}")
    private String catalogServiceUrl;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public Offer getOffer(UUID offerId) {

        String url = catalogServiceUrl + "/offers/" + offerId.toString();

        try {

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return map(response.getBody(), Offer.class);

        } catch (HttpClientErrorException e) {

            if (e.getStatusCode().is4xxClientError()) {
                throw new InvalidOfferRuntimeException(offerId, "Invalid offer id " + offerId, e);
            }

            throw new ApplicationRuntimeException("Fail getting offer " + offerId, e);

        }
    }

    @Override
    public Product getProduct(UUID productId) {

        String url = catalogServiceUrl + "/products/" + productId.toString();

        try {

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return map(response.getBody(), Product.class);

        } catch (HttpClientErrorException e) {

            if (e.getStatusCode().is4xxClientError()) {
                throw new InvalidProductRuntimeException(productId, "Invalid product id " + productId, e);
            }

            throw new ApplicationRuntimeException("Fail getting product " + productId, e);

        }
    }

    private <T> T map(String body, Class<T> valueType) {
        try {
            return mapper.readValue(body, valueType);
        } catch (JsonProcessingException e) {
            throw new BusinessRuntimeException("Fail parsing response.", e);
        }
    }
}

