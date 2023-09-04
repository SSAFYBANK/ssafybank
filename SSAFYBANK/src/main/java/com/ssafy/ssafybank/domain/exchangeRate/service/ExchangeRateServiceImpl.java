package com.ssafy.ssafybank.domain.exchangeRate.service;

import com.ssafy.ssafybank.domain.exchangeRate.dto.request.ExchangeRateRequestDto;
import com.ssafy.ssafybank.domain.exchangeRate.dto.response.ExchangeRateResponseDto;
import com.ssafy.ssafybank.domain.exchangeRate.entity.ExchangeRate;
import com.ssafy.ssafybank.domain.exchangeRate.repository.ExchangeRateRepository;
import com.ssafy.ssafybank.global.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    @Transactional
    @Override
    public List<ExchangeRateResponseDto> getExchangeRates(ExchangeRateRequestDto requestDto) {

        LocalDate startDate = requestDto.getStartDate();
        LocalDate endDate = requestDto.getEndDate();
        String exchangeCode = requestDto.getExchangeCode();
        if (startDate.isAfter(endDate)) {
            throw new CustomApiException("startDate는 endDate보다 이전이어야 합니다.");
        }

        if (exchangeCode == null || exchangeCode.trim().isEmpty()) {
            throw new CustomApiException("올바르지 않은 환율 코드입니다.");
        }

        List<ExchangeRate> exchangeRates = exchangeRateRepository
                .findByExchangeCodeAndExchangeDateBetween(exchangeCode, startDate, endDate);

        if (exchangeRates.isEmpty()) {
            throw new CustomApiException("해당 기간에 대한 환율 정보가 없습니다.");
        }

        return exchangeRates.stream()
                .map(exchangeRate -> ExchangeRateResponseDto.builder()
                        .exchangeCode(exchangeRate.getExchangeCode())
                        .exchangeDate(exchangeRate.getExchangeDate())
                        .buyExchange(exchangeRate.getBuyExchange())
                        .sellExchange(exchangeRate.getSellExchange())
                        .exchangeCountry(exchangeRate.getExchangeCountry())
                        .build())
                .collect(Collectors.toList());
    }
}

