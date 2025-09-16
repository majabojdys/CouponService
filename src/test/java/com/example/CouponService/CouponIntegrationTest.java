package com.example.CouponService;

import com.example.CouponService.commons.Error;
import com.example.CouponService.dtos.DtoCouponRequest;
import com.example.CouponService.dtos.DtoCouponUsageRequest;
import com.example.CouponService.factories.ClockFactory;
import com.example.CouponService.factories.CouponFactory;
import com.example.CouponService.repositories.Coupon;
import com.example.CouponService.repositories.CouponRepository;
import com.example.CouponService.repositories.CouponUsage;
import com.example.CouponService.repositories.CouponUsageRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;
import java.util.concurrent.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

class CouponIntegrationTest extends IntegrationTest {

    @MockitoSpyBean
    private CouponUsageRepository couponUsageRepository;

    @MockitoSpyBean
    private CouponRepository couponRepository;

    @Test
    public void addNewCouponIntegrationTestHappyPath() {
        //given
        DtoCouponRequest dtoCoupon = CouponFactory.createDefaultDtoCoupon();

        //when
        ResponseEntity<Void> response = restTemplate.postForEntity(getLocalhost() + "/api/v1/coupons", dtoCoupon, Void.class);

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Coupon> coupons = couponRepository.findAll();
        Assertions.assertEquals(1, coupons.size());
        Coupon coupon = coupons.getFirst();
        Assertions.assertEquals(dtoCoupon.couponCode(), coupon.getCouponCode());
        Assertions.assertEquals(ClockFactory.getFixedInstant(), coupon.getCreationDate());
        Assertions.assertEquals(dtoCoupon.country(), coupon.getCountry());
        Assertions.assertEquals(dtoCoupon.maxUsages(), coupon.getMaxNumberOfUses());
        Assertions.assertEquals(0, coupon.getCurrentNumberOfUses());
    }

    @Test
    public void addNewCouponIntegrationTest_CouponAlreadyExistException() {
        //given
        DtoCouponRequest dtoCoupon = CouponFactory.createDefaultDtoCoupon();
        couponRepository.save(CouponFactory.createDefaultCoupon());

        //when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/coupons", dtoCoupon, Error.class);

        //then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Assertions.assertEquals("COUPON_ALREADY_EXIST", response.getBody().errorCode());
        Assertions.assertEquals("Coupon with ID: default_coupon already exist.", response.getBody().errorDescription());
    }

    @Test
    public void useCouponIntegrationTestHappyPath() {
        //given
        Coupon coupon = CouponFactory.createDefaultCoupon();
        couponRepository.save(coupon);
        DtoCouponUsageRequest dtoCoupon = new DtoCouponUsageRequest("userId");

        //when
        ResponseEntity<Void> response = restTemplate.postForEntity(getLocalhost() + "/api/v1/coupons/default_coupon/apply", dtoCoupon, Void.class);

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Coupon result = couponRepository.findById(coupon.getCouponCode()).get();
        Assertions.assertEquals(1, result.getCurrentNumberOfUses());
    }

    @Test
    public void useCouponIntegrationTest_CouponDoesNotExistException() {
        //given
        DtoCouponUsageRequest dtoCoupon = new DtoCouponUsageRequest("userId");

        //when
        ResponseEntity<Error> response = restTemplate.postForEntity(getLocalhost() + "/api/v1/coupons/default_coupon/apply", dtoCoupon, Error.class);

        //then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("COUPON_DOES_NOT_EXIST", response.getBody().errorCode());
        Assertions.assertEquals("Coupon with ID: default_coupon doesn't exist.", response.getBody().errorDescription());
    }

    @Test
    public void useCouponIntegrationTest_CouponUsageLimitExceededException() {
        //given
        Coupon coupon = CouponFactory.createCouponWithExceededLimit();
        couponRepository.save(coupon);
        DtoCouponUsageRequest dtoCoupon = new DtoCouponUsageRequest("userId");

        //when
        ResponseEntity<Error> response = restTemplate.postForEntity(getLocalhost() + "/api/v1/coupons/default_coupon/apply", dtoCoupon, Error.class);

        //then
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("COUPON_LIMIT_REACHED", response.getBody().errorCode());
        Assertions.assertEquals("The coupon: default_coupon has already been used the maximum number of times.", response.getBody().errorDescription());
    }

    @Test
    public void useCouponIntegration_RollbackTransaction() {
        //given
        Coupon coupon = CouponFactory.createDefaultCoupon();
        couponRepository.save(coupon);
        DtoCouponUsageRequest dtoCoupon = new DtoCouponUsageRequest("userId");
        doThrow(new RuntimeException("DB fail")).when(couponUsageRepository).save(any(CouponUsage.class));

        //when
        ResponseEntity<Void> response = restTemplate.postForEntity(getLocalhost() + "/api/v1/coupons/default_coupon/apply", dtoCoupon, Void.class);

        //then
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Coupon result = couponRepository.findById(coupon.getCouponCode()).get();
        Assertions.assertEquals(0, result.getCurrentNumberOfUses());
    }

    @Test
    public void useCouponIntegration_OptimisticLockingAndRetry() throws InterruptedException, ExecutionException {
        //given
        Coupon coupon = CouponFactory.createDefaultCoupon();
        couponRepository.save(coupon);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //when
        Future<ResponseEntity<Void>> future1 = callCouponUsage(executorService, "userId1");
        Future<ResponseEntity<Void>> future2 = callCouponUsage(executorService, "userId2");
        Future<ResponseEntity<Void>> future3 = callCouponUsage(executorService, "userId3");

        //then
        Assertions.assertEquals(HttpStatus.OK, future1.get().getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, future2.get().getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, future3.get().getStatusCode());

        Coupon result = couponRepository.findById(coupon.getCouponCode()).get();
        Assertions.assertEquals(3, result.getCurrentNumberOfUses());

        List<CouponUsage> usages = couponUsageRepository.findAll();
        Assertions.assertEquals(3, usages.size());
        Assertions.assertTrue(usages.stream().anyMatch(usage -> "userId1".equals(usage.getUserId())));
        Assertions.assertTrue(usages.stream().anyMatch(usage -> "userId2".equals(usage.getUserId())));
        Assertions.assertTrue(usages.stream().anyMatch(usage -> "userId3".equals(usage.getUserId())));
    }

    @Test
    public void useCouponIntegration_OptimisticLockRetryLimitReached() {
        //given
        Coupon coupon = CouponFactory.createDefaultCoupon();
        couponRepository.save(coupon);
        DtoCouponUsageRequest dtoCoupon = new DtoCouponUsageRequest("userId");
        doThrow(new OptimisticLockException()).when(couponRepository).save(any(Coupon.class));

        //when
        ResponseEntity<Error> response = restTemplate.postForEntity(getLocalhost() + "/api/v1/coupons/default_coupon/apply", dtoCoupon, Error.class);

        //then
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        Assertions.assertEquals("COUPON_USAGE_OPTIMISTIC_RETRIES_LIMIT_REACHED", response.getBody().errorCode());
        Assertions.assertEquals("Exceeded max number of retries due to the optimistic lock for coupon default_coupon and user userId.", response.getBody().errorDescription());
    }

    private Future<ResponseEntity<Void>> callCouponUsage(ExecutorService executorService, String userId) {
        return executorService.submit(() -> restTemplate.postForEntity(getLocalhost() + "/api/v1/coupons/default_coupon/apply", new DtoCouponUsageRequest(userId), Void.class));
    }
}
