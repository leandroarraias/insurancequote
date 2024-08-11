package com.arraias.insurancequote.application.usecase.integration;

import com.arraias.insurancequote.application.domain.PolicyRequest;

public interface InsurancePolicyClient {

    void sendQuote(PolicyRequest request);

}
