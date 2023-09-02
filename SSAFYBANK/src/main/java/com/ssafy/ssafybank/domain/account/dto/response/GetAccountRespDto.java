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

    private Boolean isNext;

    private int totalCnt;
    @Builder
    public GetAccountRespDto(String accountHolderName, String bankName, String accountNum, Long balance, Boolean isNext, int totalCnt) {
        this.accountHolderName = accountHolderName;
        this.bankName = bankName;
        this.accountNum = accountNum;
        this.balance = balance;
        this.isNext = isNext;
        this.totalCnt = totalCnt;
    }
}
