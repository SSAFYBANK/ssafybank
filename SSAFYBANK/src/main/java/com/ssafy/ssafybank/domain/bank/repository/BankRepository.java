package com.ssafy.ssafybank.domain.bank.repository;

import com.ssafy.ssafybank.domain.bank.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
