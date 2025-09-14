package com.example.CouponService.dtos;

import com.example.CouponService.commons.Country;

public record DtoCouponRequest(String couponCode, Integer maxUsages, Country country) {
}
