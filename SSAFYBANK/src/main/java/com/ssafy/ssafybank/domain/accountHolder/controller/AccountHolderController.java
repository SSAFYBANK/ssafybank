package com.ssafy.ssafybank.domain.accountHolder.controller;

import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderCreate;
import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderDelete;
import com.ssafy.ssafybank.domain.accountHolder.dto.response.AccountHolderListRespDto;
import com.ssafy.ssafybank.domain.accountHolder.service.AccountHolderService;
import com.ssafy.ssafybank.global.config.auth.LoginUser;
import com.ssafy.ssafybank.global.ex.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/account-holder")
@RestController
public class AccountHolderController {

    private final AccountHolderService accountHolderService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccountHolder(@RequestBody @Valid AccountHolderCreate accountHolderCreate, @AuthenticationPrincipal LoginUser loginUser, BindingResult bindingResult ) {
        String memberUuid = loginUser.getMember().getMemberUuid(); ///강제로 준 값 로그인 구현 시 이 부분만 바뀜
        System.out.println("member" + memberUuid);
        Boolean isTrue = accountHolderService.createAccountHolder(accountHolderCreate , memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", null), HttpStatus.OK);
    }

    @GetMapping(value = "/getList", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getAccountHolderList(@AuthenticationPrincipal LoginUser loginUser) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        List<AccountHolderListRespDto> accountHolderListRespDtosList = new ArrayList<>();

        accountHolderListRespDtosList = accountHolderService.getAccountHolderList(memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", accountHolderListRespDtosList), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteAccountHolder(@RequestBody @Valid AccountHolderDelete accountHolderDelete,@AuthenticationPrincipal LoginUser loginUser, BindingResult bindingResult ) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        Boolean isTrue = accountHolderService.deleteAccountHolder(accountHolderDelete , memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", null), HttpStatus.OK);
    }

}
