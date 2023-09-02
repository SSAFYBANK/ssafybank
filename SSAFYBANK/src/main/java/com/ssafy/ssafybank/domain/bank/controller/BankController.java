package com.ssafy.ssafybank.domain.bank.controller;

import com.ssafy.ssafybank.domain.bank.dto.response.BankResponseDto;
import com.ssafy.ssafybank.domain.bank.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/bank")
@RestController
public class BankController {

    private final BankService bankService;

    @GetMapping("/list")
    public List<BankResponseDto> getAllBanks() {
        return bankService.getAllBanks();
    }
}
