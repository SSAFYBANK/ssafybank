package com.ssafy.ssafybank.domain.transfer.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.ssafybank.domain.account.entity.Account;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.transfer.entity.Transfer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransferDepositReqDto {

    @NotNull
    @NotEmpty
    @Size(max = 20)
    private String senderAccountNum;

    @NotNull
    private String senderAccountPass;

    @NotNull
    private Integer recBankCode;

    @NotNull
    @NotEmpty
    @Size(max = 20)
    private String recAccountNum;

    @NotNull
    @Max(100000000)
    @Min(1)
    private Long depositAmount;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String senderCont;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String recCont;

    public Transfer toTransferEntity(String transferUuid , Account sender , Account rec, Long senderBalance , Long recBalance){
        return Transfer.builder()
                .transferUuid(transferUuid)
                .amount(this.getDepositAmount())
                .depositAccountId(rec)
                .depositAccountBalance(recBalance)
                .withdrawAccountId(sender)
                .withdrawAccountBalance(senderBalance)
                .depositAccountContent(this.recCont)
                .withdrawAccountContent(this.senderCont)
                .build();
    }
}
