package com.arraias.insurancequote.application.usecase;

import com.arraias.insurancequote.application.domain.PolicyResponse;
import com.arraias.insurancequote.application.usecase.repository.InsuranceQuoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateQuoteUseCase {

    @Autowired
    private InsuranceQuoteRepository repository;

    public void addPolicy(PolicyResponse policyResponse) {
        log.info("Adding policy to quote: {}", policyResponse.toString());
        repository.addPolicy(policyResponse.getQuoteId(), policyResponse.getId());
    }

}
