package com.example.CouponService;

import com.example.CouponService.dtos.DtoCouponRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
class CouponController {

    private final CouponService couponService;

    CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/coupons")
    void addNewCoupon(@RequestBody @Valid DtoCouponRequest dtoCouponRequest){
        couponService.addNewCoupon(dtoCouponRequest);
    }

}
