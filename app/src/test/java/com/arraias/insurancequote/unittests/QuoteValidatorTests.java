package com.arraias.insurancequote.unittests;

import com.arraias.insurancequote.application.domain.*;
import com.arraias.insurancequote.application.exception.QuoteValidationFailedRuntimeException;
import com.arraias.insurancequote.application.usecase.validation.QuoteRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static com.arraias.insurancequote.application.domain.AssistanceType.*;
import static com.arraias.insurancequote.application.domain.CoverageType.NATURAL_DISASTERS;
import static com.arraias.insurancequote.application.domain.CoverageType.THEFT;
import static com.arraias.insurancequote.utils.MockData.*;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {QuoteRequestValidator.class})
public class QuoteValidatorTests {

    @Autowired
    private QuoteRequestValidator validator;

    private QuoteRequest quoteRequest;
    private Product product;
    private Offer offer;

    @BeforeEach
    void setUp() {
        MonthlyPremiumAmount monthlyPremium = mockMonthlyPremiumAmount();
        quoteRequest = mockQuoteRequest(randomUUID(), randomUUID(), monthlyPremium);
        offer = mockOffer(quoteRequest.getOfferId(), monthlyPremium);
        product = mockProduct(quoteRequest.getProductId(), List.of(offer.getId()));
    }

    @Test
    void shouldFailWithNullObjects() {
        var exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(null, product, offer));
        assertEquals("Quote details not provided.", exception.getMessage());

        exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(quoteRequest, null, offer));
        assertEquals("Product %s not found.".formatted(quoteRequest.getProductId()), exception.getMessage());

        exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(quoteRequest, product, null));
        assertEquals("Offer %s not found.".formatted(quoteRequest.getOfferId()), exception.getMessage());

    }

    @Test
    void shouldFailWithInactiveProduct() {
        product.setActive(false);
        var exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(quoteRequest, product, offer));
        assertEquals("Product %s (%s) is not active.".formatted(product.getName(), product.getId()), exception.getMessage());
    }

    @Test
    void shouldFailWithInactiveOffer() {
        offer.setActive(false);
        var exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(quoteRequest, product, offer));
        assertEquals("Offer %s (%s) is not active.".formatted(offer.getName(), offer.getId()), exception.getMessage());
    }

    @Test
    void shouldFailWithOfferNotBelongingToProduct() {
        product.setOffers(Collections.emptyList());
        var exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(quoteRequest, product, offer));
        assertEquals(
                "Offer %s (%s) does not belong to product %s (%s).".formatted(
                        offer.getName(),
                        offer.getId(),
                        product.getName(),
                        product.getId()),
                exception.getMessage());
    }

    @Test
    void shouldFailWithInvalidCoverage() {

        quoteRequest.setCoverages(List.of(
                new Coverage(CoverageType.NATURAL_DISASTERS, BigDecimal.valueOf(10000)),
                new Coverage(THEFT, BigDecimal.valueOf(10000))));

        var exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(quoteRequest, product, offer));
        assertEquals("Coverage %s is not present in offer %s (%s)".formatted(
                NATURAL_DISASTERS, offer.getName(), offer.getId()), exception.getMessage());
    }

    @Test
    void shouldFailWithCoverageValueAboveMaximum() {

        var reqCoverage = new Coverage(THEFT, BigDecimal.valueOf(10000.0000001));
        var offerCoverage = offer.getCoverageDetails(THEFT);

        quoteRequest.setCoverages(List.of(new Coverage(THEFT, BigDecimal.valueOf(10000.0000001))));

        var exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(quoteRequest, product, offer));

        assertEquals(
                "Coverage value %s for %s is above the maximum allowed of %s.".formatted(
                        reqCoverage.getValue(),
                        reqCoverage.getType(),
                        offerCoverage.getValue()),
                exception.getMessage());
    }

    @Test
    void shouldFailWithCoverageSumMismatch() {

        BigDecimal coveragesSum = quoteRequest.getTotalCoverageAmount();
        quoteRequest.setTotalCoverageAmount(quoteRequest.getTotalCoverageAmount().subtract(BigDecimal.ONE));

        var exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(quoteRequest, product, offer));

        assertEquals(
                "The sum of coverage (%s) does not match the total coverage amount value (%s).".formatted(
                        coveragesSum, quoteRequest.getTotalCoverageAmount()),
                exception.getMessage());

    }

    @Test
    void shouldFailWithInvalidAssistance() {
        quoteRequest.setAssistances(List.of(FUNERAL_ASSISTANCE, PLUMBER, ELECTRICIAN));
        var exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(quoteRequest, product, offer));
        assertEquals(
                "Assistance %s is not present in offer %s (%s).".formatted(
                        ELECTRICIAN, offer.getName(), offer.getId()),
                exception.getMessage());
    }

    @Test
    void shouldFailWithInvalidMonthlyPremiumAmount() {

        MonthlyPremiumAmount monthlyPremium = offer.getMonthlyPremiumAmount();

        quoteRequest.setTotalMonthlyPremiumAmount(monthlyPremium.getMaxAmount().add(new BigDecimal("0.0000000001")));
        var exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(quoteRequest, product, offer));
        assertEquals(
                "The montly premium (%s) must be between %s and %s.".formatted(
                        quoteRequest.getTotalMonthlyPremiumAmount(),
                        monthlyPremium.getMinAmount(),
                        monthlyPremium.getMaxAmount()),
                exception.getMessage());

        quoteRequest.setTotalMonthlyPremiumAmount(monthlyPremium.getMinAmount().subtract(new BigDecimal("0.0000000001")));
        exception = assertThrows(QuoteValidationFailedRuntimeException.class, () -> validator.validate(quoteRequest, product, offer));
        assertEquals(
                "The montly premium (%s) must be between %s and %s.".formatted(
                        quoteRequest.getTotalMonthlyPremiumAmount(),
                        monthlyPremium.getMinAmount(),
                        monthlyPremium.getMaxAmount()),
                exception.getMessage());
    }

    @Test
    void shouldPassWithValidRequest() {
        assertDoesNotThrow(() -> validator.validate(quoteRequest, product, offer));
    }
}
