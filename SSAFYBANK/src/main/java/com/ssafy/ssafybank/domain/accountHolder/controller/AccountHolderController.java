package com.ssafy.ssafybank.domain.accountHolder.controller;

import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderCreate;
import com.ssafy.ssafybank.domain.member.service.AccountService;
import com.ssafy.ssafybank.global.ex.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/v1")
@RestController
public class AccountHolderController {

    private final AccountService accountService;

    @PostMapping("/account-holder/create")
    public ResponseEntity<?> getProfileAndStatus(@RequestBody @Valid AccountHolderCreate accountHolderCreate, BindingResult bindingResult ) {
        String memberUuid = "1"; //강제로 준 값 로그인 구현 시 이 부분만 바뀜
        Boolean isTrue = accountService.createAccountHolder(accountHolderCreate , memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", null), HttpStatus.OK);
    }

}
