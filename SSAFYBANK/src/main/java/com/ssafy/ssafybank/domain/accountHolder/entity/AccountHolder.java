package com.ssafy.ssafybank.domain.accountHolder.entity;

import com.ssafy.ssafybank.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity
@Table(name = "account_holder")
public class AccountHolder {
	@Id
	@GeneratedValue
	private Long accountHolderId;
	@Column(nullable = false)
	private String accountHolderName;
	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdDate;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "member_id")
	private Member memberId;

	@Column(nullable = false)
	private String accountHolderUuid;

	@Column(nullable = false)
	private Boolean accountHolderStatus = false;


	@Builder
	public AccountHolder(Long accountHolderId, String accountHolderName, LocalDateTime createdDate, Member memberId, String accountHolderUuid,Boolean accountHolderStatus) {
		this.accountHolderId = accountHolderId;
		this.accountHolderName = accountHolderName;
		this.createdDate = createdDate;
		this.memberId = memberId;
		this.accountHolderUuid = accountHolderUuid;
		this.accountHolderStatus = accountHolderStatus;
	}

	public void deactivateAccountHolder() {
		this.accountHolderStatus = true;
	}
}
