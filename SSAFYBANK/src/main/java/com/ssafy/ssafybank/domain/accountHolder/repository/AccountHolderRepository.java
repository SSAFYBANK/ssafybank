package com.ssafy.ssafybank.domain.accountHolder.repository;

import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {
   Optional<List<AccountHolder>> findAccountHoldersByMemberIdAndAccountHolderStatusIsFalse(Member member);
   AccountHolder findByAccountHolderUuidAndAccountHolderStatusIsFalse(String accountHolderUuid);
   Optional<AccountHolder> findAccountHolderByMemberIdAndAccountHolderNameAndAccountHolderUuidAndAccountHolderStatusIsFalse(Member member , String AHN, String AHU);
}
