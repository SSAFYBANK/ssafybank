package com.ssafy.ssafybank.domain.transfer.controller;


import com.ssafy.ssafybank.domain.account.dto.response.PageInfo;
import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderCreate;
import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderDelete;
import com.ssafy.ssafybank.domain.member.service.KakaoLoginService;
import com.ssafy.ssafybank.domain.transfer.dto.request.GetTransferListReqDto;
import com.ssafy.ssafybank.domain.transfer.dto.request.TransferDelete;
import com.ssafy.ssafybank.domain.transfer.dto.request.TransferDepositReqDto;
import com.ssafy.ssafybank.domain.transfer.dto.response.GetTransferListRespDto;
import com.ssafy.ssafybank.domain.transfer.dto.response.TransferDepositRespDto;
import com.ssafy.ssafybank.domain.transfer.service.TransferService;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/transfer")
@RestController
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/deposit")
    public ResponseEntity<?> createTransfer(@RequestBody @Valid TransferDepositReqDto transferDepositReqDto, @AuthenticationPrincipal LoginUser loginUser, BindingResult bindingResult ) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("계좌이체를 시작한 사용자 정보 : {}", memberUuid);

        TransferDepositRespDto transferDepositRespDto = transferService.createTransfer(transferDepositReqDto, memberUuid);

        if (transferDepositRespDto != null) {
            log.info("계좌 이체 성공한 사용자 정보: {}", memberUuid);
            return new ResponseEntity<>(new ResponseDto<>(1, "성공", transferDepositRespDto), HttpStatus.OK);
        } else {
            log.error("계좌 이체를 실패한 사용자 정보 : {}", memberUuid);
            return new ResponseEntity<>(new ResponseDto<>(0, "실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getList/{page}")
    public ResponseEntity<?> getTransferList(@RequestBody @Valid GetTransferListReqDto getTransferListReqDto, @AuthenticationPrincipal LoginUser loginUser, BindingResult bindingResult, @PathVariable int page) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("계좌 이체 내역을 조회한 사용자 정보 : {}", memberUuid);

        Pageable fixedPageable = PageRequest.of(page, 10, Sort.by("createdDate").descending());
        List<GetTransferListRespDto> transferList = transferService.getTransferList(fixedPageable, getTransferListReqDto, memberUuid);
        PageInfo pageInfo = transferService.getPage(fixedPageable, memberUuid, getTransferListReqDto);

        log.info("계좌 이체 내역 조회를 성공한 사용자 정보 : {}", memberUuid);

        Map<String, Object> response = new HashMap<>();
        response.put("transfer", transferList);
        response.put("pageInfo", pageInfo);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", response), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTranser(@RequestBody @Valid TransferDelete transferDelete, @AuthenticationPrincipal LoginUser loginUser, BindingResult bindingResult ) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        log.info("계좌 이체 내역을 삭제한 사용자 정보 : {}", memberUuid);

        Boolean isTrue = transferService.deleteTransfer(transferDelete, memberUuid);

        if (isTrue) {
            log.info("계좌 이체 내역 삭제를 성공한 사용자 정보 : {}", memberUuid);
            return new ResponseEntity<>(new ResponseDto<>(1, "성공", null), HttpStatus.OK);
        } else {
            log.error("계좌 이체 내역 삭제를 실패한 사용자 정보 : {}", memberUuid);
            return new ResponseEntity<>(new ResponseDto<>(0, "실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
