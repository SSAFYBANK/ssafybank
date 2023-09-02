package com.ssafy.ssafybank.domain.bank.service;

import com.ssafy.ssafybank.domain.bank.dto.response.BankResponseDto;

import java.util.List;

public interface BankService {
    List<BankResponseDto> getAllBanks();
}
