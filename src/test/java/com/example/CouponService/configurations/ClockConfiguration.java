package com.example.CouponService.configurations;

import com.example.CouponService.factories.ClockFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class ClockConfiguration {

    @Bean
    Clock fixedClock() {
        return ClockFactory.getFixedClock();
    }
}
