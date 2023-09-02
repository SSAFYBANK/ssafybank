package com.ssafy.ssafybank.domain.bank.service;

import com.ssafy.ssafybank.domain.bank.dto.response.BankResponseDto;
import com.ssafy.ssafybank.domain.bank.entity.Bank;
import com.ssafy.ssafybank.domain.bank.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BankServiceImpl implements BankService{

    private final BankRepository bankRepository;

    @Override
    public List<BankResponseDto> getAllBanks() {
        List<Bank> banks = bankRepository.findAll();
        return banks.stream()
                .map(bank -> BankResponseDto.builder()
                        .bankName(bank.getBankName())
                        .bankCode(bank.getBankCode())
                        .build())
                .collect(Collectors.toList());
    }
}
