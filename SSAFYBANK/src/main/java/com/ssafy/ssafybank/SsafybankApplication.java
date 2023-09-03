package com.ssafy.ssafybank;

import com.ssafy.ssafybank.global.util.ExchangeRateUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.ApplicationContext;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.ssafy.ssafybank")
public class SsafybankApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = (ApplicationContext) SpringApplication.run(SsafybankApplication.class, args);
		ExchangeRateUtils exchangeRateUtils = ctx.getBean(ExchangeRateUtils.class);
		exchangeRateUtils.getExchangeRates();
	}

}
