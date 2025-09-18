package com.example.CouponService.services;

import com.example.CouponService.clients.IpCheckerClient;
import com.example.CouponService.exceptions.CouponDoesNotExistException;
import com.example.CouponService.exceptions.ForbiddenCountryException;
import com.example.CouponService.factories.CouponFactory;
import com.example.CouponService.repositories.Coupon;
import com.example.CouponService.repositories.CouponRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class IpCheckerServiceUnitTest {

    private final IpCheckerClient ipCheckerClient = Mockito.mock(IpCheckerClient.class);
    private final CouponRepository couponRepository = Mockito.mock(CouponRepository.class);

    private final IpCheckerService ipCheckerService = new IpCheckerService(ipCheckerClient, couponRepository);


    @Test
    void validateCouponCountryWithUserIpHappyPath() {
        // given
        String ip = "192.168.0.1";
        Coupon coupon = CouponFactory.createDefaultCoupon();
        Mockito.when(ipCheckerClient.getCountryByIp(ip)).thenReturn("Poland");
        Mockito.when(couponRepository.findById(coupon.getCouponCode())).thenReturn(Optional.of(coupon));

        // Mockito.when
        ipCheckerService.validateCouponCountryWithUserIp(ip, coupon.getCouponCode());

        // then - no exception thrown
        Mockito.verify(ipCheckerClient).getCountryByIp(ip);
        Mockito.verify(couponRepository).findById(coupon.getCouponCode());
    }

    @Test
    void validateCouponCountryWithUserIp_CouponDoesNotExistException() {
        // given
        String ip = "192.168.0.1";
        Coupon coupon = CouponFactory.createDefaultCoupon();
        Mockito.when(couponRepository.findById(coupon.getCouponCode())).thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> ipCheckerService.validateCouponCountryWithUserIp(ip, coupon.getCouponCode()))
                .isInstanceOf(CouponDoesNotExistException.class)
                .hasMessageContaining("Coupon with ID: default_coupon doesn't exist.");
    }

    @Test
    void validateCouponCountryWithUserIp_ForbiddenCountryException() {
        // given
        String ip = "192.168.0.1";
        Coupon coupon = CouponFactory.createDefaultCoupon();
        Mockito.when(ipCheckerClient.getCountryByIp(ip)).thenReturn("Germany");
        Mockito.when(couponRepository.findById(coupon.getCouponCode())).thenReturn(Optional.of(coupon));

        //when then
        assertThatThrownBy(() -> ipCheckerService.validateCouponCountryWithUserIp(ip, coupon.getCouponCode()))
                .isInstanceOf(ForbiddenCountryException.class)
                .hasMessageContaining("Coupon with ID: default_coupon can't be used from: Germany");

    }
}
