package com.ssafy.ssafybank.domain.bank.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity
public class Bank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private	Long bankId;
	@Column(nullable = false)
	private	String bankName;
	@Column(nullable = false)
	private	Integer bankCode;
	@Builder
	public Bank(Long bankId, String bankName, Integer bankCode) {
		this.bankId = bankId;
		this.bankName = bankName;
		this.bankCode = bankCode;
	}
}
