package com.ssafy.ssafybank.domain.account.entity;

import com.ssafy.ssafybank.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.ssafy.ssafybank.domain.accountHolder.entity.AccountHolder;
import com.ssafy.ssafybank.domain.bank.entity.Bank;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;


@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity
public class Account {
	@Id
	@GeneratedValue
	private   Long accountId;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "account_holder_id")
	private AccountHolder accountHolderId;
	@Column(nullable = false, unique = true)
	private String accountNum;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "bank_id")
	private Bank bankId;
	@Column(nullable = false)
	private   Integer accountPassword;
	@Column(nullable = false)
	private   Long balance;
	@Column(nullable = false)
	@CreatedDate
	private   LocalDateTime createdDate;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "member_id")
	private Member memberId;
	@Builder
	public Account(Long accountId, AccountHolder accountHolderId, String accountNum, Integer accountPassword,Bank bankId, Long balance, LocalDateTime createdDate, Member memberId) {
		this.accountId = accountId;
		this.accountHolderId = accountHolderId;
		this.accountNum = accountNum;
		this.bankId = bankId;
		this.accountPassword = accountPassword;
		this.balance = balance;
		this.createdDate = createdDate;
		this.memberId = memberId;
	}

	// 입금 메서드
	public void deposit(Long amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("입금 금액은 0보다 커야 합니다.");
		}
		this.balance += amount;
	}

	// 출금 메서드
	public void withdraw(Long amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("출금 금액은 0보다 커야 합니다.");
		}
		if (this.balance < amount) {
			throw new IllegalArgumentException("잔액이 부족합니다.");
		}
		this.balance -= amount;
	}
}