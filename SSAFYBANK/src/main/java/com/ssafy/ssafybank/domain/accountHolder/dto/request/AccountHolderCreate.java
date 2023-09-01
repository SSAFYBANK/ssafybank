package com.ssafy.ssafybank.domain.accountHolder.dto.request;

import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class AccountHolderCreate {

    @NotNull
    @Size(max = 20)
    private String accountHolderName;

    public AccountHolder toAccountHolderEntity(Member member){
        return AccountHolder.builder().
                accountHolderName(this.accountHolderName).
                memberId(member).
                build();
    }
}
