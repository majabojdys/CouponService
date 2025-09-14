package com.example.CouponService;

import org.springframework.boot.SpringApplication;

public class TestCouponServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(CouponServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
