package com.arraias.insurancequote.integrationtests;

import com.arraias.insurancequote.adapter.web.exceptionhandler.InvalidDataMessage;
import com.arraias.insurancequote.application.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = DEFINED_PORT)
public class BadRequestIntegrationTests {

    @Autowired
    private TestRestTemplate template;

    @Test
    void testBadRequests() {

        var request = new QuoteRequest();

        validateBadRequest(request, List.of(
                "productId", "offerId", "category", "totalMonthlyPremiumAmount", "totalCoverageAmount", "coverages",
                "assistances", "customer"));

        request = mockRequest();

        validateBadRequest(request, List.of(
                "customer.dateOfBirth", "customer.type", "customer.gender", "customer.name", "customer.email",
                "customer.documentNumber", "customer.phoneNumber"));

    }

    private QuoteRequest mockRequest() {

        var customer = new Customer("", "", "", "", "", "", "");

        return new QuoteRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                CategoryType.HOME,
                BigDecimal.TEN,
                BigDecimal.TEN,
                List.of(new Coverage(CoverageType.FIRE, BigDecimal.TEN)),
                List.of(AssistanceType.PLUMBER),
                customer);

    }

    private void validateBadRequest(QuoteRequest request, List<String> expectedInvalidData) {
        ResponseEntity<InvalidDataMessage> response = doInvalidQuote(request);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getInvalidData());
        assertEquals(expectedInvalidData.size(), response.getBody().getInvalidData().size());
        assertTrue(expectedInvalidData.containsAll(response.getBody().getInvalidData()));
    }

    private ResponseEntity<InvalidDataMessage> doInvalidQuote(QuoteRequest body) {
        HttpEntity<QuoteRequest> request = new HttpEntity<>(body);
        return template.postForEntity("http://localhost:8080/insurances-quotes", request, InvalidDataMessage.class);
    }
}
