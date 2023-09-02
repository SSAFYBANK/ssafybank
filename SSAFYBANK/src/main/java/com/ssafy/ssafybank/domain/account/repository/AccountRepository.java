package com.ssafy.ssafybank.domain.account.repository;

import com.ssafy.ssafybank.domain.account.entity.Account;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findAccountByAccountNum(String accountNum);

    Account findAccountByAccountNumAndAccountHolderId(String accountNum , AccountHolder accountHolder);

    Page<Account> findAccountsByMemberId(Member member, Pageable pageable);


    int countByMemberId(Member member);
}
