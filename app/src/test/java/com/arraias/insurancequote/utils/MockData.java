package com.arraias.insurancequote.utils;

import com.arraias.insurancequote.application.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.arraias.insurancequote.application.domain.AssistanceType.FUNERAL_ASSISTANCE;
import static com.arraias.insurancequote.application.domain.AssistanceType.PLUMBER;
import static com.arraias.insurancequote.application.domain.CoverageType.THEFT;

public class MockData {

    /** Products registered at file products-api-mapping.json */
    public static final UUID VALID_PRODUCT_A = UUID.fromString("7e8a8a99-80ba-4f40-aab5-6608ef9dafed");
    public static final UUID VALID_PRODUCT_B = UUID.fromString("562d0619-645f-4b12-a5b2-b69d6beefd12");

    /** Offers registered at file offers-api-mapping.json */
    public static final UUID VALID_OFFER_A = UUID.fromString("8452c5f3-f508-413e-b146-5e34cf1badaa");
    public static final UUID VALID_OFFER_B = UUID.fromString("a389df09-074a-423f-9b89-3bb392ab8ec2");
    public static final UUID VALID_OFFER_C = UUID.fromString("2de095f4-5f5a-4525-b1ed-42a0f859f815");

    public static MonthlyPremiumAmount mockMonthlyPremiumAmount() {
        return new MonthlyPremiumAmount(new BigDecimal("100"), new BigDecimal("50"), new BigDecimal("75"));
    }

    public static QuoteRequest mockQuoteRequest(
            UUID productId,
            UUID offerId,
            MonthlyPremiumAmount monthlyPremium) {

        QuoteRequest quoteRequest = new QuoteRequest();
        quoteRequest.setProductId(productId == null ? UUID.randomUUID(): productId);
        quoteRequest.setOfferId(offerId == null ? UUID.randomUUID() : offerId);
        quoteRequest.setCoverages(List.of(
                new Coverage(CoverageType.FIRE, BigDecimal.valueOf(10000)),
                new Coverage(THEFT, BigDecimal.valueOf(10000))));
        quoteRequest.setCategory(CategoryType.HOME);

        BigDecimal coveragesSum = BigDecimal.ZERO;

        for (var c : quoteRequest.getCoverages()) {
            coveragesSum = coveragesSum.add(c.getValue());
        }

        quoteRequest.setTotalCoverageAmount(coveragesSum);
        quoteRequest.setAssistances(List.of(FUNERAL_ASSISTANCE, PLUMBER));

        quoteRequest.setTotalMonthlyPremiumAmount(monthlyPremium == null ?
                mockMonthlyPremiumAmount().getSuggestedAmount() :
                monthlyPremium.getSuggestedAmount());

        quoteRequest.setCustomer(mockCustomer());

        return quoteRequest;
    }

    public static Offer mockOffer(UUID offerId, MonthlyPremiumAmount monthlyPremium) {

        Offer offer = new Offer();
        offer.setId(offerId == null ? UUID.randomUUID() : offerId);
        offer.setName("Offer A");
        offer.setActive(true);
        offer.setCoverages(List.of(
                new Coverage(CoverageType.FIRE, BigDecimal.valueOf(10000)),
                new Coverage(CoverageType.CIVIL_LIABILITY, BigDecimal.valueOf(10000)),
                new Coverage(THEFT, BigDecimal.valueOf(10000))));
        offer.setAssistances(List.of(FUNERAL_ASSISTANCE, PLUMBER));
        offer.setMonthlyPremiumAmount(monthlyPremium == null ? mockMonthlyPremiumAmount() : monthlyPremium);

        return offer;
    }

    public static Product mockProduct(UUID productId, List<UUID> offersId) {
        Product product = new Product();
        product.setId(productId == null ? UUID.randomUUID(): productId);
        product.setName("Product A");
        product.setActive(true);
        product.setOffers(offersId == null ? List.of(UUID.randomUUID()) : offersId);
        return product;
    }

    public static Customer mockCustomer() {
        return new Customer("123123123", "Pedro", "PF", "M", "1990-01-01", "pedro@email.com", "11989898989");
    }
}
