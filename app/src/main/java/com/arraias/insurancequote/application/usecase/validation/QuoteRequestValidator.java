package com.arraias.insurancequote.application.usecase.validation;

import com.arraias.insurancequote.application.domain.*;
import com.arraias.insurancequote.application.exception.QuoteValidationFailedRuntimeException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class QuoteRequestValidator {

    public void validate(QuoteRequest request, Product product, Offer offer) {

        validateNotNull(request, "Quote details not provided.");
        validateNotNull(product, "Product %s not found.".formatted(request.getProductId()));
        validateNotNull(offer, "Offer %s not found.".formatted(request.getOfferId()));

        if (!product.isActive()) {
            throw new QuoteValidationFailedRuntimeException(
                    "Product %s (%s) is not active.".formatted(product.getName(), product.getId()));
        }

        if (!offer.isActive()) {
            throw new QuoteValidationFailedRuntimeException(
                    "Offer %s (%s) is not active.".formatted(offer.getName(), offer.getId()));
        }

        if (!product.getOffers().contains(offer.getId())) {
            throw new QuoteValidationFailedRuntimeException(
                    "Offer %s (%s) does not belong to product %s (%s).".formatted(
                            offer.getName(),
                            offer.getId(),
                            product.getName(),
                            product.getId()));
        }

        BigDecimal coveragesSum = BigDecimal.ZERO;

        for (Coverage reqCoverage : request.getCoverages()) {

            Coverage offerCoverage = offer.getCoverageDetails(reqCoverage.getType());

            if (offerCoverage == null) {
                throw new QuoteValidationFailedRuntimeException(
                        "Coverage %s is not present in offer %s (%s)".formatted(
                                reqCoverage.getType(), offer.getName(), offer.getId()));
            }

            if (offerCoverage.getValue().compareTo(reqCoverage.getValue()) < 0) {
                throw new QuoteValidationFailedRuntimeException(
                        "Coverage value %s for %s is above the maximum allowed of %s.".formatted(
                                reqCoverage.getValue(), reqCoverage.getType(), offerCoverage.getValue()));
            }

            coveragesSum = coveragesSum.add(reqCoverage.getValue());

        }

        if (coveragesSum.compareTo(request.getTotalCoverageAmount()) != 0) {
            throw new QuoteValidationFailedRuntimeException(
                    "The sum of coverage (%s) does not match the total coverage amount value (%s).".formatted(
                            coveragesSum, request.getTotalCoverageAmount()));
        }

        for (AssistanceType reqAssistence : request.getAssistances()) {

            if (offer.getAssistances().contains(reqAssistence)) {
                continue;
            }

            throw new QuoteValidationFailedRuntimeException(
                    "Assistance %s is not present in offer %s (%s).".formatted(
                            reqAssistence, offer.getName(), offer.getId()));
        }

        BigDecimal tmpa = request.getTotalMonthlyPremiumAmount();
        MonthlyPremiumAmount mpa = offer.getMonthlyPremiumAmount();

        if (tmpa.compareTo(mpa.getMinAmount()) < 0 || tmpa.compareTo(mpa.getMaxAmount()) > 0) {
            throw new QuoteValidationFailedRuntimeException(
                    "The montly premium (%s) must be between %s and %s.".formatted(
                            tmpa, mpa.getMinAmount(), mpa.getMaxAmount()));
        }

    }

    private void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new QuoteValidationFailedRuntimeException(message);
        }
    }
}
