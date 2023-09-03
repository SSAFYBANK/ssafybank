package com.ssafy.ssafybank.domain.exchangeRate.service;

import com.ssafy.ssafybank.domain.exchangeRate.dto.request.ExchangeRateRequestDto;
import com.ssafy.ssafybank.domain.exchangeRate.dto.response.ExchangeRateResponseDto;

import java.util.List;

public interface ExchangeRateService {
    List<ExchangeRateResponseDto> getExchangeRates(ExchangeRateRequestDto requestDto);
}
