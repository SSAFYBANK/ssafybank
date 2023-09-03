package com.ssafy.ssafybank.domain.exchangeRate.controller;

import com.ssafy.ssafybank.domain.exchangeRate.dto.request.ExchangeRateRequestDto;
import com.ssafy.ssafybank.domain.exchangeRate.dto.response.ExchangeRateResponseDto;
import com.ssafy.ssafybank.domain.exchangeRate.service.ExchangeRateService;
import com.ssafy.ssafybank.global.config.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/exchangeRate")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @PostMapping("/search")
    public List<ExchangeRateResponseDto> searchExchangeRates(@RequestBody ExchangeRateRequestDto requestDto, @AuthenticationPrincipal LoginUser loginUser) {
        return exchangeRateService.getExchangeRates(requestDto);
    }
}
