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
    private String senderAccountPassword;

    @NotNull
    private Integer receiverBankCode;

    @NotNull
    @NotEmpty
    @Size(max = 20)
    private String receiverAccountNum;

    @NotNull
    @Max(100000000)
    @Min(1)
    private Long depositAmount;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String senderContent;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String receiverContent;

    public Transfer toTransferEntity(String transferUuid , Account sender , Account rec, Long senderBalance , Long recBalance){
        return Transfer.builder()
                .transferUuid(transferUuid)
                .amount(this.getDepositAmount())
                .depositAccountId(rec)
                .depositAccountBalance(recBalance)
                .withdrawAccountId(sender)
                .withdrawAccountBalance(senderBalance)
                .depositAccountContent(this.receiverContent)
                .withdrawAccountContent(this.senderContent)
                .build();
    }
}
