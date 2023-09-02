package com.ssafy.ssafybank.domain.account.controller;

import com.ssafy.ssafybank.domain.account.dto.request.AccountCreateRequestDto;
import com.ssafy.ssafybank.domain.account.dto.request.AccountGetPasswordReqDto;
import com.ssafy.ssafybank.domain.account.dto.response.AccountGetPasswordRespDto;
import com.ssafy.ssafybank.domain.account.service.AccountService;
import com.ssafy.ssafybank.domain.accountHolder.dto.response.AccountHolderListRespDto;
import com.ssafy.ssafybank.domain.bank.repository.BankRepository;
import com.ssafy.ssafybank.global.ex.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountCreateRequestDto accountCreateRequestDto, BindingResult bindingResult){
        String memberUuid = "1"; // 강제로 준 값 로그인 구현 시 이 부분만 바뀜
        Boolean isTrue = accountService.createAccount(accountCreateRequestDto, memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", null), HttpStatus.OK);
    }

    @PostMapping("/getPassword")
    public ResponseEntity<?> getPassword(@RequestBody @Valid AccountGetPasswordReqDto accountGetPasswordReqDto, BindingResult bindingResult){
        String memberUuid = "1"; // 강제로 준 값 로그인 구현 시 이 부분만 바뀜

        AccountGetPasswordRespDto accountCreateRequestDto  = accountService.getPassword(accountGetPasswordReqDto, memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", accountCreateRequestDto), HttpStatus.OK);
    }
}
