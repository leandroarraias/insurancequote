package com.arraias.insurancequote.integrationtests;

import com.arraias.insurancequote.adapter.web.exceptionhandler.ErrorMessage;
import com.arraias.insurancequote.application.domain.QuoteRequest;
import com.arraias.insurancequote.application.domain.QuoteResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import static com.arraias.insurancequote.utils.MockData.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@SpringBootTest(webEnvironment = DEFINED_PORT)
public class RequestQuoteIntegrationTests {

    @Autowired
    private TestRestTemplate template;

    @Test
    void shouldFailWithInvalidProduct() {
        QuoteRequest body = mockQuoteRequest(null, null, null);
        HttpEntity<QuoteRequest> request = new HttpEntity<>(body);
        ResponseEntity<ErrorMessage> response = template.postForEntity("http://localhost:8080/insurances-quotes", request, ErrorMessage.class);
        Assertions.assertEquals(UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void shouldFailWithInvalidOffer() {
        QuoteRequest body = mockQuoteRequest(VALID_PRODUCT_A, null, null);
        HttpEntity<QuoteRequest> request = new HttpEntity<>(body);
        ResponseEntity<ErrorMessage> response = template.postForEntity("http://localhost:8080/insurances-quotes", request, ErrorMessage.class);
        Assertions.assertEquals(UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void shouldFailWithInvalidRelantionshipProductOffer() {
        QuoteRequest body = mockQuoteRequest(VALID_PRODUCT_A, VALID_OFFER_C, null);
        HttpEntity<QuoteRequest> request = new HttpEntity<>(body);
        ResponseEntity<ErrorMessage> response = template.postForEntity("http://localhost:8080/insurances-quotes", request, ErrorMessage.class);
        Assertions.assertEquals(UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void shouldCreateQuoteSuccessfully() {
        QuoteRequest body = mockQuoteRequest(VALID_PRODUCT_A, VALID_OFFER_A, null);
        HttpEntity<QuoteRequest> request = new HttpEntity<>(body);
        ResponseEntity<QuoteResponse> response = template.postForEntity("http://localhost:8080/insurances-quotes", request, QuoteResponse.class);
        Assertions.assertEquals(CREATED, response.getStatusCode());
    }

}
