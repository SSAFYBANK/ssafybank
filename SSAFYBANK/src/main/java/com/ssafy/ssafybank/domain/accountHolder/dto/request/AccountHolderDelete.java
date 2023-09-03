package com.ssafy.ssafybank.domain.accountHolder.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountHolderDelete {

    @NotNull
    @Size(max = 20)
    private String accountHolderName;


    @NotNull
    @Size(max = 100)
    private String accountHolderToken;


}
