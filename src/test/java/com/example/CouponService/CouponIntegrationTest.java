package com.example.CouponService;

import com.example.CouponService.commons.Error;
import com.example.CouponService.dtos.DtoCouponRequest;
import com.example.CouponService.dtos.DtoCouponUsageRequest;
import com.example.CouponService.factories.ClockFactory;
import com.example.CouponService.factories.CouponFactory;
import com.example.CouponService.repositories.Coupon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

class CouponIntegrationTest extends IntegrationTest {

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
        DtoCouponUsageRequest dtoCoupon = new DtoCouponUsageRequest("user_id");

        //when
        ResponseEntity<Void> response = restTemplate.postForEntity(getLocalhost() + "/api/v1/coupons/default_coupon/apply", dtoCoupon, Void.class);

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Coupon result = couponRepository.findById(coupon.getCouponCode()).get();
        Assertions.assertEquals(1, result.getCurrentNumberOfUses());
    }

}
