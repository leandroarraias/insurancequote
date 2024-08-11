package com.arraias.insurancequote.adapter.persistence.repository;

import com.arraias.insurancequote.adapter.persistence.entity.InsuranceQuoteEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface InsuranceQuoteCrudRepository extends CrudRepository<InsuranceQuoteEntity, UUID> {
}
