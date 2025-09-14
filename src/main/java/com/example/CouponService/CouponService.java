package com.example.CouponService;

import com.example.CouponService.dtos.DtoCouponRequest;
import com.example.CouponService.exceptions.CouponAlreadyExistException;
import com.example.CouponService.repositories.Coupon;
import com.example.CouponService.repositories.CouponRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final Clock clock;

    public CouponService(CouponRepository couponRepository, Clock clock) {
        this.couponRepository = couponRepository;
        this.clock = clock;
    }

    public void addNewCoupon(DtoCouponRequest dtoCouponRequest) {
        if(couponRepository.existsByCouponCodeIgnoreCase(dtoCouponRequest.couponCode())){
            throw new CouponAlreadyExistException(dtoCouponRequest.couponCode());
        }
        couponRepository.save(new Coupon(dtoCouponRequest.couponCode(), Instant.now(clock), dtoCouponRequest.maxUsages(), dtoCouponRequest.country()));
    }
}
