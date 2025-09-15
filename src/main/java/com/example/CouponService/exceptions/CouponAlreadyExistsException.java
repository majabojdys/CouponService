package com.example.CouponService.exceptions;

public class CouponAlreadyExistsException extends RuntimeException {

    public CouponAlreadyExistsException(String couponCode) {
        super(String.format("Coupon with ID: %s already exist.", couponCode));
    }
}
