package com.example.CouponService.factories;

import com.example.CouponService.commons.Country;
import com.example.CouponService.dtos.DtoCouponRequest;
import com.example.CouponService.repositories.Coupon;

public class CouponFactory {

    public static DtoCouponRequest createDefaultDtoCoupon() {
        return new DtoCouponRequest("default_coupon", Country.POLAND, 3);
    }

    public static Coupon createDefaultCoupon() {
        return new Coupon("default_coupon", ClockFactory.getFixedInstant(), Country.POLAND, 3);
    }

    public static Coupon createCouponWithExceededLimit() {
        Coupon coupon = createDefaultCoupon();
        coupon.setCurrentNumberOfUses(coupon.getMaxNumberOfUses()+1);
        return coupon;
    }

    public static Coupon createCouponWithEqualNumberOfUsagesAsMaxNumberOfUsages() {
        Coupon coupon = createDefaultCoupon();
        coupon.setCurrentNumberOfUses(coupon.getMaxNumberOfUses());
        return coupon;
    }

}
