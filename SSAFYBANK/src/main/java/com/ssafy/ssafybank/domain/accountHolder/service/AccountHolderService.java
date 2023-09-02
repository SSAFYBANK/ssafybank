package com.ssafy.ssafybank.domain.accountHolder.service;

import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderCreate;

public interface AccountHolderService {
    Boolean createAccountHolder(AccountHolderCreate accountHolderCreate, String memberUuid);
}
