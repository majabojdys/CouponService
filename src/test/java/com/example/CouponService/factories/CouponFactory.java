package com.example.CouponService.factories;

import com.example.CouponService.commons.Country;
import com.example.CouponService.dtos.DtoCouponRequest;
import com.example.CouponService.repositories.Coupon;

public class CouponFactory {

    public static DtoCouponRequest createDefaultDtoCoupon() {
        return new DtoCouponRequest("default coupon", Country.POLAND, 3);
    }

    public static Coupon createDefaultCoupon() {
        return new Coupon("default coupon", ClockFactory.getFixedInstant(), Country.POLAND, 3);
    }
}
