package com.ssafy.ssafybank.domain.account.controller;

import com.ssafy.ssafybank.domain.account.dto.request.AccountCreateRequestDto;
import com.ssafy.ssafybank.domain.account.dto.request.AccountDeleteRequestDto;
import com.ssafy.ssafybank.domain.account.dto.request.AccountGetBalanceReqDto;
import com.ssafy.ssafybank.domain.account.dto.request.AccountGetPasswordReqDto;
import com.ssafy.ssafybank.domain.account.dto.response.AccountGetBalanceRespDto;
import com.ssafy.ssafybank.domain.account.dto.response.AccountGetPasswordRespDto;
import com.ssafy.ssafybank.domain.account.dto.response.GetAccountRespDto;
import com.ssafy.ssafybank.domain.account.dto.response.PageInfo;
import com.ssafy.ssafybank.domain.account.service.AccountService;
import com.ssafy.ssafybank.global.config.auth.LoginUser;
import com.ssafy.ssafybank.global.ex.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(
            @RequestBody @Valid AccountCreateRequestDto accountCreateRequestDto,
            @AuthenticationPrincipal LoginUser loginUser,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ResponseDto<>(500, "유효성 검사 실패", null), HttpStatus.BAD_REQUEST);
        }

        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("AccountController_ createAccount -> 계좌 생성한 사용자 정보 : {}", memberUuid);
        Boolean accountCreated = accountService.createAccount(accountCreateRequestDto, memberUuid);

        if (accountCreated) {
            return new ResponseEntity<>(new ResponseDto<>(200, "성공", null), HttpStatus.CREATED);
        } else {
            log.error("계좌 생성 실패한 사용자 정보 : {}",memberUuid);
            return new ResponseEntity<>(new ResponseDto<>(500, "계좌 생성 실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getPassword")
    public ResponseEntity<?> getPassword(@RequestBody @Valid AccountGetPasswordReqDto accountGetPasswordReqDto, @AuthenticationPrincipal LoginUser loginUser, BindingResult bindingResult) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("AccountController_getPassword -> 비밀번호 찾기 시작한 사용자 정보 : {}", memberUuid);
        AccountGetPasswordRespDto accountCreateRequestDto = accountService.getPassword(accountGetPasswordReqDto, memberUuid);
        log.info("AccountController_getPassword -> 비밀번호 찾기 완료한 사용자 정보 : {}", memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", accountCreateRequestDto), HttpStatus.OK);
    }

    @GetMapping(value = "/getAccountList/{page}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getAccountList(@PathVariable int page, @AuthenticationPrincipal LoginUser loginUser) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("AccountController_getAccountList -> 계좌 목록 조회 시작한 사용자 정보 : {}", memberUuid);
        Pageable fixedPageable = PageRequest.of(page, 10, Sort.by("accountId").ascending());

        List<GetAccountRespDto> getAccountRespDtos = accountService.getAccountList(fixedPageable, memberUuid);
        log.debug("계좌 목록 조회: {}", getAccountRespDtos);

        PageInfo pageInfo = accountService.getPageInfo(fixedPageable, memberUuid);

        Map<String, Object> response = new HashMap<>();
        response.put("accounts", getAccountRespDtos);
        response.put("pageInfo", pageInfo);

        log.info("AccountController_getAccountList -> 계좌 목록 조회 완료한 사용자 정보 : {}", memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", response), HttpStatus.OK);
    }

    @GetMapping(value = "/getHolderAccountList/{accountHolderUuid}/{page}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getHolderAccountList(@PathVariable String accountHolderUuid, @PathVariable int page, @AuthenticationPrincipal LoginUser loginUser) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("AccountController_getHolderAccountList -> 보유 계좌 목록 조회 시작한 사용자 정보 : {}", memberUuid);
        Pageable fixedPageable = PageRequest.of(page, 10, Sort.by("accountId").ascending());
        List<GetAccountRespDto> getAccountRespDtos = accountService.getHolderAccountList(fixedPageable, memberUuid, accountHolderUuid);
        log.debug("예금주 목록 조회: {}", getAccountRespDtos);

        PageInfo pageInfo = accountService.getPageInfoHolder(fixedPageable, memberUuid, accountHolderUuid);
        Map<String, Object> response = new HashMap<>();
        response.put("accounts", getAccountRespDtos);
        response.put("pageInfo", pageInfo);
        log.info("AccountController_getHolderAccountList -> 보유 계좌 목록 조회 완료한 사용자 정보 : {}", memberUuid);

        return new ResponseEntity<>(new ResponseDto<>(1, "성공", response), HttpStatus.OK);
    }


    @PostMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestBody @Valid AccountDeleteRequestDto accountDeleteRequestDto, @AuthenticationPrincipal LoginUser loginUser, BindingResult bindingResult) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("AccountController_deleteAccount -> 계좌 삭제 시작한 사용자 정보 : {}", memberUuid);
        Boolean isSuccess = accountService.deleteAccount(accountDeleteRequestDto, memberUuid);
        if (isSuccess) {
            log.info("AccountController_deleteAccount -> 계좌 삭제 성공한 사용자 정보 : {}", memberUuid);
            return new ResponseEntity<>(new ResponseDto<>(1, "계좌 삭제 성공", null), HttpStatus.OK);
        } else {
            log.error("AccountController_deleteAccount -> 계좌 삭제 실패한 사용자 정보 : {}", memberUuid);
            return new ResponseEntity<>(new ResponseDto<>(0, "계좌 삭제 실패", null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/getBalance")
    public ResponseEntity<?> getBalance(@RequestBody @Valid AccountGetBalanceReqDto accountGetBalanceReqDto, @AuthenticationPrincipal LoginUser loginUser, BindingResult bindingResult) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("AccountController_getBalance -> 계좌 잔액 조회 시작한 사용자 정보 : {}", memberUuid);
        AccountGetBalanceRespDto accountGetBalanceRespDto = accountService.getBalance(accountGetBalanceReqDto, memberUuid);
        log.info("AccountController_getBalance -> 계좌 잔액 조회 완료한 사용자 정보 : {}", memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", accountGetBalanceRespDto), HttpStatus.OK);
    }
}
