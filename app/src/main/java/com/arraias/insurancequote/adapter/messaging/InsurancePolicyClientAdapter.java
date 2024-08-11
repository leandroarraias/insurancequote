package com.arraias.insurancequote.adapter.messaging;

import com.arraias.insurancequote.application.domain.PolicyRequest;
import com.arraias.insurancequote.application.exception.ApplicationRuntimeException;
import com.arraias.insurancequote.application.usecase.integration.InsurancePolicyClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class InsurancePolicyClientAdapter implements InsurancePolicyClient {

    @Autowired
    private Queue queue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void sendQuote(PolicyRequest request) {
        jmsTemplate.convertAndSend(queue, parseToJSON(request));
    }

    private String parseToJSON(PolicyRequest request) {
        try {
            return mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new ApplicationRuntimeException("Error parsing object to json format: %s".formatted(request), e);
        }
    }
}
