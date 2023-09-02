package com.ssafy.ssafybank.domain.accountHolder.service;

import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderCreate;
import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderDelete;
import com.ssafy.ssafybank.domain.accountHolder.dto.response.AccountHolderListRespDto;

import java.util.List;

public interface AccountHolderService {
    Boolean createAccountHolder(AccountHolderCreate accountHolderCreate, String memberUuid);

    AccountHolderListRespDto getAccountHolderList(String memberUuid);

    Boolean deleteAccountHolder(AccountHolderDelete accountHolderDelete, String memberUuid);
}
