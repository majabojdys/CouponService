package com.example.CouponService.exceptions;

public class CouponDoesNotExistException extends RuntimeException {

  public CouponDoesNotExistException(String couponCode) {
    super(String.format("Coupon with ID: %s doesn't exist.", couponCode));
  }
}
