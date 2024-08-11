package com.arraias.insurancequote.adapter.messaging;

import com.arraias.insurancequote.application.domain.PolicyRequest;
import com.arraias.insurancequote.application.domain.PolicyResponse;
import com.arraias.insurancequote.application.exception.ApplicationRuntimeException;
import com.arraias.insurancequote.application.usecase.UpdateQuoteUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ThreadUtils;
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
    public void listener(String policy) throws InterruptedException {
        Thread.sleep(15000); // Only simulating the response delay from external system
        PolicyResponse response = parseFromJSON(policy);
        response.setId(UUID.randomUUID());
        log.info("Receiving policy for %s".formatted(response.toString()));
        updateQuoteUseCase.addPolicy(response);
    }

    private PolicyResponse parseFromJSON(String jsonResponse) {
        try {
            return mapper.readValue(jsonResponse, PolicyResponse.class);
        } catch (JsonProcessingException e) {
            throw new ApplicationRuntimeException("Error parsing object from json format: %s".formatted(jsonResponse), e);
        }
    }
}
