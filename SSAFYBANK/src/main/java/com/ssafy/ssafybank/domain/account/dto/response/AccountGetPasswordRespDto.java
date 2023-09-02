package com.ssafy.ssafybank.domain.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountGetPasswordRespDto {

    private String password;

    private String accountNum;
}
