package com.ssafy.ssafybank.domain.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountDeleteRequestDto {
    @NotNull
    @NotEmpty
    private String accountHolderUuid;
    @NotNull
    private Integer bankCode;
    @NotNull
    private String accountNum;
    @NotNull
    private Integer accountPassword;
}
