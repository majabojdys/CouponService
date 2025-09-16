package com.example.CouponService.exceptions;

public class CouponUsageOptimisticLockException extends RuntimeException {

    public CouponUsageOptimisticLockException(String couponCode, String userId) {
        super(String.format("Exceeded max number of retries due to the optimistic lock for coupon %s and user %s.", couponCode, userId));
    }
}
