package com.ssafy.ssafybank.domain.account.repository;

import com.ssafy.ssafybank.domain.account.entity.Account;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findAccountByAccountNum(String accountNum);

    Account findAccountByAccountNumAndAccountHolderId(String accountNum , AccountHolder accountHolder);

}
