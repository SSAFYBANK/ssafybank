package com.ssafy.ssafybank.domain.account.repository;

import com.ssafy.ssafybank.domain.account.entity.Account;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findAccountByAccountNumAndAccountStatusIsFalse(String accountNum);

    Account findAccountByAccountNumAndAccountHolderIdAndAccountStatusIsFalse(String accountNum , AccountHolder accountHolder);

    Page<Account> findAccountsByMemberIdAndAccountStatusIsFalse(Member member, Pageable pageable);

    Page<Account> findAccountsByAccountHolderIdAndAccountStatusIsFalse(AccountHolder accountHolder , Pageable pageable);

    int countByMemberIdAndAccountStatusIsFalse(Member member);

    int countByAccountHolderIdAndAccountStatusIsFalse(AccountHolder accountHolder);

    List<Account> findAccountsByAccountHolderIdAndAccountStatusIsFalse(AccountHolder accountHolder);
}
