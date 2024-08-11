package com.arraias.insurancequote.application.usecase.repository;

import com.arraias.insurancequote.application.domain.QuoteRequest;
import com.arraias.insurancequote.application.domain.QuoteResponse;

import java.util.UUID;

public interface InsuranceQuoteRepository {

    QuoteResponse saveQuote(QuoteRequest request);

    QuoteResponse searchQuote(UUID quoteId);

    void addPolicy(UUID quoteId, UUID policyId);

}
