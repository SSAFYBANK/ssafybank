package com.ssafy.ssafybank.domain.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountGetPasswordReqDto {
    @NotNull
    @NotEmpty
    private String accountHolderUuid;
    @NotNull
    private String accountNum;
}
