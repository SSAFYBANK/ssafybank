package com.ssafy.ssafybank.domain.transfer.repository;


import com.ssafy.ssafybank.domain.account.entity.Account;
import com.ssafy.ssafybank.domain.transfer.entity.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    @Query("SELECT t FROM Transfer t WHERE t.depositAccountId = :account OR t.withdrawAccountId = :account")
    Page<Transfer> findTransfersByDepositAccountIdOrWithdrawAccountId(@Param("account") Account account, Pageable pageable);


    @Query("SELECT COUNT(t) FROM Transfer t WHERE t.depositAccountId = :account OR t.withdrawAccountId = :account")
    int countByDepositAccountIdOrWithdrawAccountId(@Param("account") Account account);

    Transfer findTransfersByTransferUuid(String transferUuid);
}
