package com.ssafy.ssafybank.domain.transfer.controller;


import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderCreate;
import com.ssafy.ssafybank.domain.member.service.KakaoLoginService;
import com.ssafy.ssafybank.domain.transfer.dto.request.TransferDepositReqDto;
import com.ssafy.ssafybank.domain.transfer.dto.response.TransferDepositRespDto;
import com.ssafy.ssafybank.domain.transfer.service.TransferService;
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
@RequestMapping("/v1/transfer")
@RestController
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/deposit")
    public ResponseEntity<?> createTransfer(@RequestBody @Valid TransferDepositReqDto transferDepositReqDto, BindingResult bindingResult ) {

        String memberUuid = "1"; //강제로 준 값 로그인 구현 시 이 부분만 바뀜
        TransferDepositRespDto transferDepositRespDto = transferService.createTransfer(transferDepositReqDto , memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", transferDepositRespDto), HttpStatus.OK);
    }
}
