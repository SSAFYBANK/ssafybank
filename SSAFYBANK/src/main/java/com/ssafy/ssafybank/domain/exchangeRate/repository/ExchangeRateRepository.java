package com.ssafy.ssafybank.domain.exchangeRate.repository;

import com.ssafy.ssafybank.domain.exchangeRate.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findByExchangeCodeAndExchangeDateBetween(
            String exchangeCountry, LocalDate startDate, LocalDate endDate);


}
