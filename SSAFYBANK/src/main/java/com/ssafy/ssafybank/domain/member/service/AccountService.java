package com.ssafy.ssafybank.domain.member.service;

import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderCreate;

public interface AccountService {
    Boolean createAccountHolder(AccountHolderCreate accountHolderCreate, String memberUuid);
}
