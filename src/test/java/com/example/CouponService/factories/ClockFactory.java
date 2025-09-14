package com.example.CouponService.factories;

import java.time.*;

public class ClockFactory {

    private static Instant fixedInstant = LocalDateTime.of(2025, 5, 7, 20, 23,3).toInstant(ZoneOffset.UTC);

    public static Clock getFixedClock(){
        return getFixedClock(fixedInstant);
    }

    public static Clock getFixedClock(Instant fixedTime){
        return Clock.fixed(fixedTime, ZoneOffset.systemDefault());
    }

    public static Instant getFixedInstant() {
        return fixedInstant;
    }

}
