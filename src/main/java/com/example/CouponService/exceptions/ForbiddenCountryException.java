package com.example.CouponService.exceptions;

public class ForbiddenCountryException extends RuntimeException {

    public ForbiddenCountryException(String couponCode, String userCountry) {
        super(String.format("Coupon with ID: %s can't be used from: %s", couponCode, userCountry));
    }
}
