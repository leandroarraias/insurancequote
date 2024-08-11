package com.arraias.insurancequote.application.usecase;

import com.arraias.insurancequote.application.domain.*;
import com.arraias.insurancequote.application.usecase.integration.CatalogServiceClient;
import com.arraias.insurancequote.application.usecase.integration.InsurancePolicyClient;
import com.arraias.insurancequote.application.usecase.repository.InsuranceQuoteRepository;
import com.arraias.insurancequote.application.usecase.validation.QuoteRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateQuoteUseCase {

    @Autowired
    private CatalogServiceClient catalogService;

    @Autowired
    private InsuranceQuoteRepository repository;

    @Autowired
    private InsurancePolicyClient insurancePolicyClient;

    @Autowired
    private QuoteRequestValidator validator;

    public QuoteResponse create(QuoteRequest request) {
        Product product = catalogService.getProduct(request.getProductId());
        Offer offer = catalogService.getOffer(request.getOfferId());
        validator.validate(request, product, offer);
        QuoteResponse quoteResponse = repository.saveQuote(request);
        insurancePolicyClient.sendQuote(new PolicyRequest(quoteResponse.getId()));
        return quoteResponse;
    }
}
