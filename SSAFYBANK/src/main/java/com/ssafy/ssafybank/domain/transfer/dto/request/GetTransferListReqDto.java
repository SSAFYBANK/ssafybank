package com.ssafy.ssafybank.domain.transfer.dto.request;

import com.ssafy.ssafybank.domain.account.entity.Account;
import com.ssafy.ssafybank.domain.transfer.entity.Transfer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetTransferListReqDto {

    @NotNull
    @NotEmpty
    @Size(max = 20)
    private String accountNum;

    @NotNull
    private String accountPassword;


}
