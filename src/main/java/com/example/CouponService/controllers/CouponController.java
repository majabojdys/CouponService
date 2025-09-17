package com.example.CouponService.controllers;

import com.example.CouponService.facades.CouponFacade;
import com.example.CouponService.commons.IpUtils;
import com.example.CouponService.dtos.DtoCouponRequest;
import com.example.CouponService.dtos.DtoCouponUsageRequest;
import com.example.CouponService.services.CouponService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
class CouponController {

    private final CouponService couponService;
    private final CouponFacade couponFacade;

    public CouponController(CouponService couponService, CouponFacade couponFacade) {
        this.couponService = couponService;
        this.couponFacade = couponFacade;
    }

    @PostMapping("/coupons")
    void addNewCoupon(@RequestBody @Valid DtoCouponRequest dtoCouponRequest){
        couponService.addNewCoupon(dtoCouponRequest);
    }

    @PostMapping("/coupons/{couponCode}/apply")
    void useCoupon(@PathVariable String couponCode,
                   @RequestBody DtoCouponUsageRequest dtoCouponUsageRequest,
                   HttpServletRequest request){
        couponFacade.useCoupon(couponCode, dtoCouponUsageRequest.userId(), IpUtils.getClientIp(request));
    }

}
