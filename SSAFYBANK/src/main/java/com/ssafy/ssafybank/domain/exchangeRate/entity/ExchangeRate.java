package com.ssafy.ssafybank.domain.exchangeRate.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity
@Table(name = "exchange_rate")
public class ExchangeRate {
	@GeneratedValue
	@Id
	private Long exchangeId;
	@Column(nullable = false)
	private	String exchangeCountry;
	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime exchangeDate;
	@Column(nullable = false)
	private	String buyExchange;
	@Column(nullable = false)
	private	String sellExchange;

	@Builder

	public ExchangeRate(Long exchangeId, String exchangeCountry, LocalDateTime exchangeDate, String buyExchange, String sellExchange) {
		this.exchangeId = exchangeId;
		this.exchangeCountry = exchangeCountry;
		this.exchangeDate = exchangeDate;
		this.buyExchange = buyExchange;
		this.sellExchange = sellExchange;
	}
}
