package com.ssafy.ssafybank.domain.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountGetBalanceReqDto {
    @NotNull
    @NotEmpty
    @Size(max = 5)
    private String accountPassword;
    @NotNull
    @NotEmpty
    @Size(max = 20)
    private String accountNum;
}
