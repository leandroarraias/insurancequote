package com.arraias.insurancequote.integrationtests;

import com.arraias.insurancequote.adapter.web.exceptionhandler.ErrorMessage;
import com.arraias.insurancequote.application.domain.QuoteRequest;
import com.arraias.insurancequote.application.domain.QuoteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.arraias.insurancequote.utils.MockData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = DEFINED_PORT)
public class SearchQuoteIntegrationTests {

    @Autowired
    private TestRestTemplate template;

    @Test
    void shouldFailWithInvalidQuote() {
        String searchUrl = "http://localhost:8080/insurances-quotes/%s".formatted(UUID.randomUUID());
        var response = template.getForEntity(searchUrl, ErrorMessage.class);
        assertEquals(UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void shouldCreateAndSearchQuoteSuccessfully() throws InterruptedException {

        QuoteRequest body = mockQuoteRequest(VALID_PRODUCT_A, VALID_OFFER_A, null);
        HttpEntity<QuoteRequest> request = new HttpEntity<>(body);

        var createResponse = template.postForEntity("http://localhost:8080/insurances-quotes", request, QuoteResponse.class);
        assertEquals(CREATED, createResponse.getStatusCode());
        var createBodyResponse = createResponse.getBody();

        assert createBodyResponse != null;
        String searchUrl = "http://localhost:8080/insurances-quotes/%s".formatted(createBodyResponse.getId());

        Thread.sleep(2000); // Wait until the policy would be updated into quote register

        var searchResponse = template.getForEntity(searchUrl, QuoteResponse.class);
        assertEquals(OK, searchResponse.getStatusCode());
        var searchBodyResponse = searchResponse.getBody();

        validateResponse(createBodyResponse, searchBodyResponse);

    }

    private void validateResponse(QuoteResponse createResponse, QuoteResponse searchResponse) {

        assertNotNull(createResponse);
        assertNotNull(searchResponse);

        assertEquals(createResponse.getId(), searchResponse.getId());
        assertEquals(createResponse.getProductId(), searchResponse.getProductId());
        assertEquals(createResponse.getOfferId(), searchResponse.getOfferId());
        assertEquals(createResponse.getCategory(), searchResponse.getCategory());
        assertEquals(0, createResponse.getTotalCoverageAmount().compareTo(searchResponse.getTotalCoverageAmount()));
        assertEquals(0, createResponse.getTotalMonthlyPremiumAmount().compareTo(searchResponse.getTotalMonthlyPremiumAmount()));
        assertEquals(format(createResponse.getCreatedAt()), format(searchResponse.getCreatedAt()));

        assertNull(createResponse.getUpdatedAt());
        assertNotNull(searchResponse.getUpdatedAt());

    }

    private String format(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return dateTime.format(formatter);
    }
}
