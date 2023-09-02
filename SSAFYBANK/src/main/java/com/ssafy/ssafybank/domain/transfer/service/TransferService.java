package com.ssafy.ssafybank.domain.transfer.service;

import com.ssafy.ssafybank.domain.transfer.dto.request.TransferDepositReqDto;
import com.ssafy.ssafybank.domain.transfer.dto.response.TransferDepositRespDto;

public interface TransferService {
    TransferDepositRespDto createTransfer(TransferDepositReqDto transferDepositReqDto, String memberUuid);
}
