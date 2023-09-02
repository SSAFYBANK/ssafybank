package com.ssafy.ssafybank.domain.transfer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class GetTransferListRespDto {

    private LocalDateTime transferDate;

    private String flag;

    private Long amount;

    private String content;

    private Long afterAmount;

    private String transferUuid;
    @Builder
    public GetTransferListRespDto(LocalDateTime transferDate, String flag, Long amount, String content, Long afterAmount , String transferUuid) {
        this.transferDate = transferDate;
        this.flag = flag;
        this.amount = amount;
        this.content = content;
        this.afterAmount = afterAmount;
        this.transferUuid = transferUuid;
    }
}
