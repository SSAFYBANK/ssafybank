package com.ssafy.ssafybank.domain.exchangeRate.repository;

import com.ssafy.ssafybank.domain.exchangeRate.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
}
