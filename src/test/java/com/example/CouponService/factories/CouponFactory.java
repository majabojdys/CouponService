package com.example.CouponService.factories;

import com.example.CouponService.commons.Country;
import com.example.CouponService.dtos.DtoCouponRequest;
import com.example.CouponService.repositories.Coupon;

public class CouponFactory {

    public static DtoCouponRequest createDefaultDtoCoupon() {
        return new DtoCouponRequest("default coupon", 3, Country.POLAND);
    }

    public static Coupon createDefaultCoupon() {
        return new Coupon("default coupon", ClockFactory.getFixedInstant(), 3, Country.POLAND);
    }
}
