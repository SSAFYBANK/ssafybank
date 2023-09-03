package com.ssafy.ssafybank.domain.exchangeRate.dto.response;

import lombok.*;

import java.time.LocalDate;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResponseDto {
    private String exchangeCode;
    private LocalDate exchangeDate;
    private String buyExchange;
    private String sellExchange;
    private String exchangeCountry;
}
