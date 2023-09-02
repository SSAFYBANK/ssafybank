package com.ssafy.ssafybank.domain.account.controller;

import com.ssafy.ssafybank.domain.account.dto.request.AccountCreateRequestDto;
import com.ssafy.ssafybank.domain.account.dto.request.AccountGetPasswordReqDto;
import com.ssafy.ssafybank.domain.account.dto.response.AccountGetPasswordRespDto;
import com.ssafy.ssafybank.domain.account.dto.response.GetAccountRespDto;
import com.ssafy.ssafybank.domain.account.dto.response.PageInfo;
import com.ssafy.ssafybank.domain.account.service.AccountService;
import com.ssafy.ssafybank.domain.accountHolder.dto.response.AccountHolderListRespDto;
import com.ssafy.ssafybank.domain.bank.repository.BankRepository;
import com.ssafy.ssafybank.global.ex.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @GetMapping(value = "/getAccountList/{page}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getAccountList(@PathVariable int page) {

        String memberUuid = "1";
        Pageable fixedPageable = PageRequest.of(page, 10, Sort.by("accountId").ascending());
        List<GetAccountRespDto> getAccountRespDtos = new ArrayList<>();
        getAccountRespDtos = accountService.getAccountList(fixedPageable, memberUuid);
        PageInfo pageInfo = accountService.getPageInfo(fixedPageable, memberUuid);

        Map<String, Object> response = new HashMap<>();
        response.put("accounts", getAccountRespDtos);
        response.put("pageInfo", pageInfo);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", response), HttpStatus.OK);
    }


    @GetMapping(value = "/getHolderAccountList/{accountHolderUuid}/{page}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getHolderAccountList( @PathVariable String accountHolderUuid,@PathVariable int page) {

        String memberUuid = "1";
        Pageable fixedPageable = PageRequest.of(page, 10, Sort.by("accountId").ascending());
        List<GetAccountRespDto> getAccountRespDtos = new ArrayList<>();
        getAccountRespDtos = accountService.getHolderAccountList(fixedPageable, memberUuid,accountHolderUuid);
        PageInfo pageInfo = accountService.getPageInfoHolder(fixedPageable, memberUuid,accountHolderUuid);

        Map<String, Object> response = new HashMap<>();
        response.put("accounts", getAccountRespDtos);
        response.put("pageInfo", pageInfo);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", response), HttpStatus.OK);
    }
}
