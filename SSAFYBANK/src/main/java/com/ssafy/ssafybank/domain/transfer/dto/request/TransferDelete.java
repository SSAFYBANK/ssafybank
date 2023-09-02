package com.ssafy.ssafybank.domain.transfer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransferDelete {
    @NotNull
    @NotEmpty
    private String transferUuid;
}
