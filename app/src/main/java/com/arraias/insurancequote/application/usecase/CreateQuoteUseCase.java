package com.arraias.insurancequote.application.usecase;

import com.arraias.insurancequote.application.domain.*;
import com.arraias.insurancequote.application.exception.InvalidOfferRuntimeException;
import com.arraias.insurancequote.application.exception.InvalidProductRuntimeException;
import com.arraias.insurancequote.application.exception.InvalidQuoteRequestRuntimeException;
import com.arraias.insurancequote.application.usecase.integration.CatalogServiceClient;
import com.arraias.insurancequote.application.usecase.integration.InsurancePolicyClient;
import com.arraias.insurancequote.application.usecase.repository.InsuranceQuoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class CreateQuoteUseCase {

    @Autowired
    private CatalogServiceClient catalogService;

    @Autowired
    private InsuranceQuoteRepository repository;

    @Autowired
    private InsurancePolicyClient insurancePolicyClient;

    public QuoteResponse create(QuoteRequest request) {
        Product product = catalogService.getProduct(request.getProductId());
        Offer offer = catalogService.getOffer(request.getOfferId());
        validate(request, product, offer);
        QuoteResponse quoteResponse = repository.saveQuote(request);
        insurancePolicyClient.sendQuote(new PolicyRequest(quoteResponse.getId()));
        return quoteResponse;
    }

    private void validate(QuoteRequest request, Product product, Offer offer) {

        if (!product.isActive()) {
            throw new InvalidProductRuntimeException(product.getId(), "Product is not active.");
        }

        if (!product.getOffers().contains(offer.getId())) {
            throw new InvalidOfferRuntimeException(offer.getId(), "Offer does not belong to product " + product.getId());
        }

        if (!offer.isActive()) {
            throw new InvalidOfferRuntimeException(offer.getId(), "Offer is not active.");
        }

        BigDecimal coveragesSum = BigDecimal.ZERO;

        for (Coverage reqCoverage : request.getCoverages()) {

            Coverage offerCoverage = offer.getCoverageDetails(reqCoverage.getType());

            if (offerCoverage == null) {
                throw new InvalidQuoteRequestRuntimeException(
                        "Coverage %s is not present in offer %s".formatted(reqCoverage.getType(), offer.getId()));
            }

            if (offerCoverage.getValue().compareTo(reqCoverage.getValue()) < 0) {
                throw new InvalidQuoteRequestRuntimeException(
                        "Coverage value %s for %s is above the maximum allowed of %s.".formatted(
                                reqCoverage.getValue(), reqCoverage.getType(), offerCoverage.getValue()));
            }

            coveragesSum = coveragesSum.add(reqCoverage.getValue());

        }

        if (coveragesSum.compareTo(request.getTotalCoverageAmount()) != 0) {
            throw new InvalidQuoteRequestRuntimeException(
                    "The sum of coverage (%s) does not match the total coverage amount value (%s).".formatted(
                            coveragesSum, request.getTotalCoverageAmount()));
        }

        for (AssistanceType reqAssistence : request.getAssistances()) {

            if (offer.getAssistances().contains(reqAssistence)) {
                continue;
            }

            throw new InvalidQuoteRequestRuntimeException(
                    "Assistance %s is not present in offer %s".formatted(reqAssistence, offer.getId()));
        }

        BigDecimal tmpa = request.getTotalMonthlyPremiumAmount();
        MonthlyPremiumAmount mpa = offer.getMonthlyPremiumAmount();

        if (tmpa.compareTo(mpa.getMinAmount()) < 0 || tmpa.compareTo(mpa.getMaxAmount()) > 0) {
            throw new InvalidQuoteRequestRuntimeException(
                    "The montly premium must be between %s and %s".formatted(mpa.getMinAmount(), mpa.getMaxAmount()));
        }

    }
}
