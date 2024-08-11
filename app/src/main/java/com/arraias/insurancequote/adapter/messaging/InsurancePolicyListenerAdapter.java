package com.arraias.insurancequote.adapter.messaging;

import com.arraias.insurancequote.application.domain.PolicyResponse;
import com.arraias.insurancequote.application.usecase.UpdateQuoteUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class InsurancePolicyListenerAdapter {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UpdateQuoteUseCase updateQuoteUseCase;

    @JmsListener(destination = "insurance-policy-queue")
    public void listener(String policy) throws InterruptedException, JsonProcessingException {
        PolicyResponse response = mapper.readValue(policy, PolicyResponse.class);
        response.setId(UUID.randomUUID());
        log.info("Receiving policy for %s".formatted(response.toString()));
        updateQuoteUseCase.addPolicy(response);
    }
}
