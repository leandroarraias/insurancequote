package com.arraias.insurancequote.adapter.persistence;

import com.arraias.insurancequote.adapter.persistence.entity.InsuranceQuoteEntity;
import com.arraias.insurancequote.adapter.persistence.mapper.InsuranceQuoteMapper;
import com.arraias.insurancequote.adapter.persistence.repository.InsuranceQuoteCrudRepository;
import com.arraias.insurancequote.application.domain.QuoteRequest;
import com.arraias.insurancequote.application.domain.QuoteResponse;
import com.arraias.insurancequote.application.exception.BusinessRuntimeException;
import com.arraias.insurancequote.application.usecase.repository.InsuranceQuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InsuranceQuoteRepositoryAdapter implements InsuranceQuoteRepository {

    @Autowired
    private InsuranceQuoteCrudRepository crudRepository;

    @Autowired
    private InsuranceQuoteMapper mapper;

    @Override
    public QuoteResponse saveQuote(QuoteRequest request) {
        InsuranceQuoteEntity entity = mapper.toEntity(request);
        crudRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public QuoteResponse searchQuote(UUID quoteId) {
        InsuranceQuoteEntity entity = crudRepository.findById(quoteId).orElse(null);
        return mapper.toDomain(entity);
    }

    @Override
    public void addPolicy(UUID quoteId, UUID policyId) {
        InsuranceQuoteEntity entity = crudRepository.findById(quoteId).orElseThrow();
        entity.setInsurancePolicyId(policyId);
        entity.setUpdatedAt(LocalDateTime.now());
        crudRepository.save(entity);
    }
}
