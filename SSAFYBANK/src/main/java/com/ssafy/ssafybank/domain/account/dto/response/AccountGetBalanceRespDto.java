package com.ssafy.ssafybank.domain.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AccountGetBalanceRespDto {

    private String bankName;

    private String accountNum;

    private Long balance;

    private String accountHolderName;

    @Builder
    public AccountGetBalanceRespDto(String bankName, String accountNum, Long balance, String accountHolderName) {
        this.bankName = bankName;
        this.accountNum = accountNum;
        this.balance = balance;
        this.accountHolderName = accountHolderName;
    }
}
