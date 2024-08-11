package com.arraias.insurancequote.adapter.persistence.mapper;

import com.arraias.insurancequote.adapter.persistence.entity.AssistanceEntity;
import com.arraias.insurancequote.adapter.persistence.entity.CoverageEntity;
import com.arraias.insurancequote.adapter.persistence.entity.InsuranceQuoteEntity;
import com.arraias.insurancequote.application.domain.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public abstract class InsuranceQuoteMapper {

    public InsuranceQuoteEntity toEntity(QuoteRequest request) {
        InsuranceQuoteEntity entity = quoteRequestToInsuranceQuoteEntity(request);
        entity.getCustomer().setInsuranceQuote(entity);
        entity.getCoverages().forEach(c -> c.setInsuranceQuote(entity));
        entity.getAssistances().forEach(a -> a.setInsuranceQuote(entity));
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    public QuoteResponse toDomain(InsuranceQuoteEntity entity) {
        return insuranceQuoteEntityToQuoteResponse(entity);
    }

    @Mapping(source = "category", target = "category", qualifiedByName = "categoryTypeToString")
    protected abstract InsuranceQuoteEntity quoteRequestToInsuranceQuoteEntity(QuoteRequest request);

    @Mapping(source = "category", target = "category", qualifiedByName = "stringToCategoryType")
    protected abstract QuoteResponse insuranceQuoteEntityToQuoteResponse(InsuranceQuoteEntity entity);

    @Mapping(source = "value", target = "coverageValue")
    protected abstract CoverageEntity coverageToCoverageEntity(Coverage coverage);

    @Mapping(source = "coverageValue", target = "value")
    protected abstract Coverage coverageEntityToCoverage(CoverageEntity coverage);

    protected AssistanceEntity assistanceTypeToAssistanceEntity(AssistanceType assistance) {
        AssistanceEntity entity = new AssistanceEntity();
        entity.setAssistance(assistance.name());
        return entity;
    }

    protected AssistanceType assistanceEntityToAssistanceType(AssistanceEntity entity) {
        return AssistanceType.valueOf(entity.getAssistance());
    }

    @Named("stringToCategoryType")
    protected CategoryType stringToCategoryType(String category) {
        return category != null ? CategoryType.valueOf(category) : null;
    }

    @Named("categoryTypeToString")
    protected String categoryTypeToString(CategoryType category) {
        return category != null ? category.name() : null;
    }
}
