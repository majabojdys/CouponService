package com.example.CouponService.dtos;

import com.example.CouponService.commons.Country;

public record DtoCouponRequest(String couponCode, Country country, Integer maxUsages) {
}
