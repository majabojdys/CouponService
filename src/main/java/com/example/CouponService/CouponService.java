package com.example.CouponService;

import com.example.CouponService.dtos.DtoCouponRequest;
import com.example.CouponService.dtos.DtoCouponUsageRequest;
import com.example.CouponService.exceptions.CouponAlreadyExistsException;
import com.example.CouponService.exceptions.CouponDoesNotExistException;
import com.example.CouponService.exceptions.CouponUsageLimitExceededException;
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
            throw new CouponAlreadyExistsException(dtoCouponRequest.couponCode());
        }
        couponRepository.save(new Coupon(dtoCouponRequest.couponCode(), Instant.now(clock), dtoCouponRequest.country(), dtoCouponRequest.maxUsages()));
    }

    public void useCoupon(String couponCode, String userId) {
        Coupon coupon = couponRepository.findById(couponCode).orElseThrow(() -> new CouponDoesNotExistException(couponCode));
        if (coupon.getCurrentNumberOfUses() >= coupon.getMaxNumberOfUses()){
            throw new CouponUsageLimitExceededException(couponCode);
        }
        coupon.setCurrentNumberOfUses(coupon.getCurrentNumberOfUses()+1);
        couponRepository.save(coupon);
    }
}
