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

@RequiredArgsConstructor
@RequestMapping("/v1/transfer")
@RestController
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/deposit")
    public ResponseEntity<?> createTransfer(@RequestBody @Valid TransferDepositReqDto transferDepositReqDto, @AuthenticationPrincipal LoginUser loginUser, BindingResult bindingResult ) {

        String memberUuid = loginUser.getMember().getMemberUuid();
        TransferDepositRespDto transferDepositRespDto = transferService.createTransfer(transferDepositReqDto , memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", transferDepositRespDto), HttpStatus.OK);
    }

    @PostMapping("/getList/{page}")
    public ResponseEntity<?> getTransferList(@RequestBody @Valid GetTransferListReqDto getTransferListReqDto, @AuthenticationPrincipal LoginUser loginUser,BindingResult bindingResult ,@PathVariable int page) {

        String memberUuid = loginUser.getMember().getMemberUuid();
        Pageable fixedPageable = PageRequest.of(page, 10, Sort.by("createdDate").descending());
        List<GetTransferListRespDto> transferList  = transferService.getTransferList(fixedPageable, getTransferListReqDto , memberUuid);

        PageInfo pageInfo = transferService.getPage(fixedPageable, memberUuid,getTransferListReqDto);

        Map<String, Object> response = new HashMap<>();
        response.put("transfer", transferList);
        response.put("pageInfo", pageInfo);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", response), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTranser(@RequestBody @Valid TransferDelete transferDelete, @AuthenticationPrincipal LoginUser loginUser,BindingResult bindingResult ) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        Boolean isTrue = transferService.deleteTransfer(transferDelete , memberUuid);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", null), HttpStatus.OK);
    }
}
