package com.ssafy.ssafybank.domain.bank.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankResponseDto {
    private Integer bankCode;
    private String bankName;
}
