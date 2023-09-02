package com.ssafy.ssafybank.domain.transfer.controller;


import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderCreate;
import com.ssafy.ssafybank.domain.member.service.KakaoLoginService;
import com.ssafy.ssafybank.global.ex.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/v1")
@RestController
public class TransferController {

    @PostMapping("/transfer/deposit")
    public ResponseEntity<?> createAccountHolder(@RequestBody @Valid AccountHolderCreate accountHolderCreate, BindingResult bindingResult ) {
        System.out.println("ww" + accountHolderCreate.getAHN());
        String memberUuid = "1"; //강제로 준 값 로그인 구현 시 이 부분만 바뀜
      //  Boolean isTrue = accountHolderService.createAccountHolder(accountHolderCreate , memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", null), HttpStatus.OK);
    }
}
