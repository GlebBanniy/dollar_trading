package com.dollartrading.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TradingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingApplication.class, args);
	}

}
