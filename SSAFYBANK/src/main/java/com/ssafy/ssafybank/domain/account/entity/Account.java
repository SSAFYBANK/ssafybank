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
	private	Long accountId;
	@Column(nullable = false)
	private String accountUuid;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "account_holder_id")
	private AccountHolder accountHolderId;
	@Column(nullable = false)
	private Integer accountNum;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "bank_id")
	private Bank bankId;
	@Column(nullable = false)
	private	Long balance;
	@Column(nullable = false)
	@CreatedDate
	private	LocalDateTime createdDate;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "member_id")
	private Member memberId;
	@Builder
	public Account(Long accountId, String accountUuid, AccountHolder accountHolderId, Integer accountNum, Bank bankId, Long balance, LocalDateTime createdDate, Member memberId) {
		this.accountId = accountId;
		this.accountUuid = accountUuid;
		this.accountHolderId = accountHolderId;
		this.accountNum = accountNum;
		this.bankId = bankId;
		this.balance = balance;
		this.createdDate = createdDate;
		this.memberId = memberId;
	}
}
