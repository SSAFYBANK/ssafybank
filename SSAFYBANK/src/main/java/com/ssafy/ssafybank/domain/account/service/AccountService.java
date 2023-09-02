package com.ssafy.ssafybank.domain.account.service;

import com.ssafy.ssafybank.domain.account.dto.request.AccountCreateRequestDto;

public interface AccountService {
   Boolean createAccount(AccountCreateRequestDto requestDto, String memberUuid);
}

