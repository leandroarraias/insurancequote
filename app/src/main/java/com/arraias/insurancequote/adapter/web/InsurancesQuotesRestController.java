package com.arraias.insurancequote.adapter.web;

import com.arraias.insurancequote.application.domain.QuoteRequest;
import com.arraias.insurancequote.application.domain.QuoteResponse;
import com.arraias.insurancequote.application.usecase.CreateQuoteUseCase;
import com.arraias.insurancequote.application.usecase.SearchQuoteUseCase;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/insurances-quotes")
@Slf4j
public class InsurancesQuotesRestController {

    @Autowired
    private CreateQuoteUseCase createQuoteUseCase;

    @Autowired
    private SearchQuoteUseCase searchQuoteUseCase;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<QuoteResponse> requestQuote(@Valid @RequestBody QuoteRequest request) {
        log.info("Quote request received: {}", request);
        QuoteResponse quoteResponse = createQuoteUseCase.create(request);
        return new ResponseEntity<>(quoteResponse, CREATED);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<QuoteResponse> searchQuote(@PathVariable UUID id) {
        log.info("Quote search received: {}", id);
        QuoteResponse quoteResponse = searchQuoteUseCase.searchQuote(id);
        return new ResponseEntity<>(quoteResponse, OK);
    }
}
