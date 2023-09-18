package com.ssafy.ssafybank.domain.accountHolder.controller;

import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderCreate;
import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderDelete;
import com.ssafy.ssafybank.domain.accountHolder.dto.response.AccountHolderListRespDto;
import com.ssafy.ssafybank.domain.accountHolder.service.AccountHolderService;
import com.ssafy.ssafybank.global.config.auth.LoginUser;
import com.ssafy.ssafybank.global.ex.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/account-holder")
@RestController
public class AccountHolderController {

    private final AccountHolderService accountHolderService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccountHolder(@RequestBody @Valid AccountHolderCreate accountHolderCreate, @AuthenticationPrincipal LoginUser loginUser, BindingResult bindingResult ) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("AccountHolderController_createAccountHolder -> {}", memberUuid);
        Boolean isTrue = accountHolderService.createAccountHolder(accountHolderCreate , memberUuid);
        if (isTrue) {
            log.info("예금주 생성을 성공한 사용자 정보 : {}", memberUuid);
            return new ResponseEntity<>(new ResponseDto<>(1, "성공", null), HttpStatus.OK);
        } else {
            log.error("예금주 생성 실패한 사용자 정보 : {}", memberUuid);
            return new ResponseEntity<>(new ResponseDto<>(0, "예금주 생성 실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getList", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getAccountHolderList(@AuthenticationPrincipal LoginUser loginUser) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("사용자별 예금주 조회한 사용자 정보 : {}", memberUuid);
        List<AccountHolderListRespDto> accountHolderListRespDtosList = accountHolderService.getAccountHolderList(memberUuid);
        log.info("사용자별 예금주 조회 성공한 사용자 정보 : {}", memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", accountHolderListRespDtosList), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteAccountHolder(@RequestBody @Valid AccountHolderDelete accountHolderDelete, @AuthenticationPrincipal LoginUser loginUser, BindingResult bindingResult ) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("예금주 삭제 시작한 사용자 정보 : {}", memberUuid);
        log.info("삭제 할 예금주 : {}", accountHolderDelete);
        Boolean isTrue = accountHolderService.deleteAccountHolder(accountHolderDelete , memberUuid);
        if (isTrue) {
            log.info("예금주 삭제 성공한 사용자 정보 : {}", memberUuid);
            return new ResponseEntity<>(new ResponseDto<>(1, "성공", null), HttpStatus.OK);
        } else {
            log.error("예금주 삭제 실패한 사용자 정보 : {}", memberUuid);
            return new ResponseEntity<>(new ResponseDto<>(0, "계좌 삭제 실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
