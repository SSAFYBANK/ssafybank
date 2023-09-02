package com.ssafy.ssafybank.domain.accountHolder.dto.response;

import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountHolderListRespDto {

    private String accountHolderName;

    private String accountHolderUuid;
}
