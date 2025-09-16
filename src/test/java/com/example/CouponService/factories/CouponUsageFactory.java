package com.example.CouponService.factories;

import com.example.CouponService.repositories.Coupon;
import com.example.CouponService.repositories.CouponUsage;

public class CouponUsageFactory {

    public static CouponUsage createDefaultCouponUsage(Coupon coupon) {
        return new CouponUsage("userId", ClockFactory.getFixedInstant(), coupon);
    }
}
