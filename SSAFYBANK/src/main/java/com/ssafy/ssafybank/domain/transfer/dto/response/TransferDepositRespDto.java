package com.ssafy.ssafybank.domain.transfer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransferDepositRespDto {

    private String recName;

    private Long depositAmount;

}
