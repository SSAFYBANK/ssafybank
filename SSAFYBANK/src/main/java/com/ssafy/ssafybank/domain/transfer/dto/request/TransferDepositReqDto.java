package com.ssafy.ssafybank.domain.transfer.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransferDepositReqDto {
    @JsonProperty("AHN")
    @NotNull
    @NotEmpty
    @Size(max = 20)
    private String AHN;


    public AccountHolder toAccountHolderEntity(Member member , String accountHolderUuid){
        return AccountHolder.builder().
                accountHolderName(this.AHN).
                memberId(member).
                accountHolderUuid(accountHolderUuid).
                build();
    }
}
