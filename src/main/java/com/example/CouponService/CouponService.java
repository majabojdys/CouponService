package com.example.CouponService;

import com.example.CouponService.dtos.DtoCouponRequest;
import com.example.CouponService.exceptions.CouponAlreadyExistsException;
import com.example.CouponService.exceptions.CouponDoesNotExistException;
import com.example.CouponService.exceptions.CouponUsageLimitExceededException;
import com.example.CouponService.exceptions.CouponUsageOptimisticLockException;
import com.example.CouponService.repositories.Coupon;
import com.example.CouponService.repositories.CouponRepository;
import com.example.CouponService.repositories.CouponUsage;
import com.example.CouponService.repositories.CouponUsageRepository;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final Clock clock;

    public CouponService(CouponRepository couponRepository, CouponUsageRepository couponUsageRepository, Clock clock) {
        this.couponRepository = couponRepository;
        this.couponUsageRepository = couponUsageRepository;
        this.clock = clock;
    }

    public void addNewCoupon(DtoCouponRequest dtoCouponRequest) {
        if (couponRepository.existsByCouponCodeIgnoreCase(dtoCouponRequest.couponCode())) {
            throw new CouponAlreadyExistsException(dtoCouponRequest.couponCode());
        }
        couponRepository.save(new Coupon(dtoCouponRequest.couponCode(), Instant.now(clock), dtoCouponRequest.country(), dtoCouponRequest.maxUsages()));
    }

    @Retryable(retryFor = {OptimisticLockException.class, StaleObjectStateException.class, ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 200)
    )
    @Transactional
    public void useCoupon(String couponCode, String userId) {
        Coupon coupon = couponRepository.findById(couponCode).orElseThrow(() -> new CouponDoesNotExistException(couponCode));
        if (coupon.getCurrentNumberOfUses() >= coupon.getMaxNumberOfUses()) {
            throw new CouponUsageLimitExceededException(couponCode);
        }
        coupon.setCurrentNumberOfUses(coupon.getCurrentNumberOfUses() + 1);
        couponRepository.save(coupon);
        CouponUsage couponUsage = new CouponUsage(userId, Instant.now(clock), coupon);
        couponUsageRepository.save(couponUsage);
    }

    @Recover
    private void recoverCouponUsage(Exception e, String couponCode, String userId) {
        throw new CouponUsageOptimisticLockException(couponCode, userId);
    }
}
