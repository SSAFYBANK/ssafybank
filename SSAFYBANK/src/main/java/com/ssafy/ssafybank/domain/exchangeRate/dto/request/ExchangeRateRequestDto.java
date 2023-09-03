package com.ssafy.ssafybank.domain.exchangeRate.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ssafy.ssafybank.global.util.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExchangeRateRequestDto {
    @NotNull
    private String exchangeCode;

    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;

    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;
}

