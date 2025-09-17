package com.example.CouponService.facades;

import com.example.CouponService.services.CouponService;
import com.example.CouponService.services.IpCheckerService;
import org.springframework.stereotype.Component;

@Component
public class CouponFacade {

    private final IpCheckerService ipCheckerService;
    private final CouponService couponService;

    public CouponFacade(IpCheckerService ipCheckerService, CouponService couponService) {
        this.ipCheckerService = ipCheckerService;
        this.couponService = couponService;
    }

    public void useCoupon(String couponCode, String userId, String ip){
        ipCheckerService.validateCouponCountryWithUserIp(ip, couponCode);
        couponService.useCoupon(couponCode, userId);
    }

}
