package com.ssafy.ssafybank.domain.account.service;

import com.ssafy.ssafybank.domain.account.dto.request.AccountCreateRequestDto;
import com.ssafy.ssafybank.domain.account.dto.request.AccountDeleteRequestDto;
import com.ssafy.ssafybank.domain.account.dto.request.AccountGetBalanceReqDto;
import com.ssafy.ssafybank.domain.account.dto.request.AccountGetPasswordReqDto;
import com.ssafy.ssafybank.domain.account.dto.response.AccountGetBalanceRespDto;
import com.ssafy.ssafybank.domain.account.dto.response.AccountGetPasswordRespDto;
import com.ssafy.ssafybank.domain.account.dto.response.GetAccountRespDto;
import com.ssafy.ssafybank.domain.account.dto.response.PageInfo;
import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderDelete;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
   Boolean createAccount(AccountCreateRequestDto requestDto, String memberUuid);

    AccountGetPasswordRespDto getPassword(AccountGetPasswordReqDto accountGetPasswordReqDto, String memberUuid);

    List<GetAccountRespDto> getAccountList(Pageable page, String memberUuid);

    PageInfo getPageInfo(Pageable fixedPageable, String memberUuid);

    List<GetAccountRespDto> getHolderAccountList(Pageable fixedPageable, String memberUuid, String accountHolderUuid);

    PageInfo getPageInfoHolder(Pageable fixedPageable, String memberUuid, String accountHolderUuid);

    Boolean deleteAccount(AccountDeleteRequestDto accountDeleteRequestDto, String memberUuid);

    AccountGetBalanceRespDto getBalance(AccountGetBalanceReqDto accountGetBalanceReqDto, String memberUuid);
}

