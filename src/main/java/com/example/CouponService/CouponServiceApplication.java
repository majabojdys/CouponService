package com.example.CouponService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class CouponServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponServiceApplication.class, args);
	}

}
