package com.example.CouponService.exceptions;

public class CouponAlreadyExistException extends RuntimeException {
    public CouponAlreadyExistException(String couponId) {
        super(String.format("Coupon with ID: %s already exist.", couponId));
    }
}
