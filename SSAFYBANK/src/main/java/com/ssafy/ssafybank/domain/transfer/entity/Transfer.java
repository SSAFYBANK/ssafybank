package com.ssafy.ssafybank.domain.transfer.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.ssafy.ssafybank.domain.account.entity.Account;
import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity
public class Transfer {
	@GeneratedValue
	@Id
	private Long transferId;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "withdraw_account_id")
	private Account withdrawAccountId;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "deposita_ccount_id")
	private Account depositAccountId;
	@Column(nullable = false)
	private int amount;
	@Column(nullable = false)
	private String flag;
	@Column
	private Long withdrawAccountBalance;
	@Column
	private Long depositAccountBalance;
	@Column(nullable = false)
	private LocalDateTime createdDate;

	@Builder
	public Transfer(Long transferId, Account withdrawAccountId, Account depositAccountId, int amount, String flag, Long withdrawAccountBalance, Long depositAccountBalance, LocalDateTime createdDate) {
		this.transferId = transferId;
		this.withdrawAccountId = withdrawAccountId;
		this.depositAccountId = depositAccountId;
		this.amount = amount;
		this.flag = flag;
		this.withdrawAccountBalance = withdrawAccountBalance;
		this.depositAccountBalance = depositAccountBalance;
		this.createdDate = createdDate;
	}
}
