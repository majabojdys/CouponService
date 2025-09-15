package com.example.CouponService.exceptions;

public class CouponUsageLimitExceededException extends RuntimeException {

  public CouponUsageLimitExceededException(String couponCode) {
    super(String.format("The coupon: %s has already been used the maximum number of times.", couponCode));
  }
}
