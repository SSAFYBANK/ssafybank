package com.ssafy.ssafybank.domain.account.repository;

import com.ssafy.ssafybank.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
