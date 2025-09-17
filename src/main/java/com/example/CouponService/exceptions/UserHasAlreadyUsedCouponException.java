package com.example.CouponService.exceptions;

public class UserHasAlreadyUsedCouponException extends RuntimeException {

  public UserHasAlreadyUsedCouponException(String couponCode, String userId) {
    super(String.format("User with ID: %s has already used coupon: %s.", userId, couponCode));
  }
}
