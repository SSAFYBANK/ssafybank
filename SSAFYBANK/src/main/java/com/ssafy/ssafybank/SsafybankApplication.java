package com.ssafy.ssafybank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SsafybankApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsafybankApplication.class, args);
	}

}
