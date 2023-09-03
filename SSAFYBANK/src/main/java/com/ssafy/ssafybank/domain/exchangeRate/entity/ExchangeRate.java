package com.ssafy.ssafybank.domain.exchangeRate.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "exchange_rate")
public class ExchangeRate {
	@GeneratedValue
	@Id
	private Long exchangeId;
	@Column(nullable = false)
	private	String exchangeCountry;
	@Column(nullable = false)
	private LocalDate exchangeDate;
	@Column(nullable = false)
	private	String buyExchange;
	@Column(nullable = false)
	private	String sellExchange;
	@Column(nullable = false)
	private String exchangeCode;
	@Builder
	public ExchangeRate(Long exchangeId, String exchangeCountry, LocalDate exchangeDate, String buyExchange, String sellExchange, String exchangeCode) {
		this.exchangeId = exchangeId;
		this.exchangeCountry = exchangeCountry;
		this.exchangeDate = exchangeDate;
		this.buyExchange = buyExchange;
		this.sellExchange = sellExchange;
		this.exchangeCode = exchangeCode;
	}
}
