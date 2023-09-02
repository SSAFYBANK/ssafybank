package com.ssafy.ssafybank.domain.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PageInfo {

    private boolean isNext;
    private int totalCnt;
}
