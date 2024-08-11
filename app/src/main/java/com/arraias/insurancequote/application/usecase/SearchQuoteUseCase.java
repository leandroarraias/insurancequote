package com.arraias.insurancequote.application.usecase;

import com.arraias.insurancequote.application.domain.Offer;
import com.arraias.insurancequote.application.domain.Product;
import com.arraias.insurancequote.application.domain.QuoteRequest;
import com.arraias.insurancequote.application.domain.QuoteResponse;
import com.arraias.insurancequote.application.exception.InvalidQuoteRequestRuntimeException;
import com.arraias.insurancequote.application.usecase.repository.InsuranceQuoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class SearchQuoteUseCase {

    @Autowired
    private InsuranceQuoteRepository repository;

    public QuoteResponse searchQuote(UUID quoteId) {
        log.info("Searching quote by id %s".formatted(quoteId));
        QuoteResponse quoteResponse = repository.searchQuote(quoteId);

        if (quoteResponse == null) {
            throw new InvalidQuoteRequestRuntimeException("Quote of id %s not found.".formatted(quoteId));
        }

        return quoteResponse;
    }
}
