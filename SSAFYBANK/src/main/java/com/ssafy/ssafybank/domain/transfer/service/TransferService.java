package com.ssafy.ssafybank.domain.transfer.service;

import com.ssafy.ssafybank.domain.account.dto.response.PageInfo;
import com.ssafy.ssafybank.domain.transfer.dto.request.GetTransferListReqDto;
import com.ssafy.ssafybank.domain.transfer.dto.request.TransferDelete;
import com.ssafy.ssafybank.domain.transfer.dto.request.TransferDepositReqDto;
import com.ssafy.ssafybank.domain.transfer.dto.response.GetTransferListRespDto;
import com.ssafy.ssafybank.domain.transfer.dto.response.TransferDepositRespDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransferService {
    TransferDepositRespDto createTransfer(TransferDepositReqDto transferDepositReqDto, String memberUuid);

    List<GetTransferListRespDto> getTransferList(Pageable fixedPageable, GetTransferListReqDto getTransferListReqDto, String memberUuid);

    PageInfo getPage(Pageable fixedPageable, String memberUuid, GetTransferListReqDto getTransferListReqDto);

    Boolean deleteTransfer(TransferDelete transferDelete, String memberUuid);
}
