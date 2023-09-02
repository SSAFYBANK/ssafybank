package com.ssafy.ssafybank.domain.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetAccountRespDto {


    private String accountHolderName;

    private String bankName;

    private String accountNum;

    private Long balance;


    @Builder
    public GetAccountRespDto(String accountHolderName, String bankName, String accountNum, Long balance) {
        this.accountHolderName = accountHolderName;
        this.bankName = bankName;
        this.accountNum = accountNum;
        this.balance = balance;

    }
}
