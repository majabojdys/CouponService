package com.example.CouponService.exceptions;

public class IpCheckerException extends RuntimeException {

    public IpCheckerException(String ip) {
        super(String.format("Could not check country for IP: %s.", ip));
    }
}
