package com.ssafy.ssafybank.domain.transfer.repository;


import com.ssafy.ssafybank.domain.transfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
