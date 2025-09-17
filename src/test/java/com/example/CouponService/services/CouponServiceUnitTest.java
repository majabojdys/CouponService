package com.example.CouponService.services;

import com.example.CouponService.dtos.DtoCouponRequest;
import com.example.CouponService.exceptions.CouponAlreadyExistsException;
import com.example.CouponService.exceptions.CouponDoesNotExistException;
import com.example.CouponService.exceptions.CouponUsageLimitExceededException;
import com.example.CouponService.exceptions.UserHasAlreadyUsedCouponException;
import com.example.CouponService.factories.ClockFactory;
import com.example.CouponService.factories.CouponFactory;
import com.example.CouponService.factories.CouponUsageFactory;
import com.example.CouponService.repositories.Coupon;
import com.example.CouponService.repositories.CouponRepository;
import com.example.CouponService.repositories.CouponUsage;
import com.example.CouponService.repositories.CouponUsageRepository;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.time.Clock;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


public class CouponServiceUnitTest {

    private final CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
    private final CouponUsageRepository couponUsageRepository = Mockito.mock(CouponUsageRepository.class);
    private final Clock clock = ClockFactory.getFixedClock();

    private final CouponService couponService = new CouponService(couponRepository, couponUsageRepository, clock);

    @Test
    public void addNewCouponTestHappyPath() {
        //given
        DtoCouponRequest dtoCoupon = CouponFactory.createDefaultDtoCoupon();
        Mockito.when(couponRepository.existsByCouponCodeIgnoreCase(dtoCoupon.couponCode())).thenReturn(false);

        //when
        couponService.addNewCoupon(dtoCoupon);

        //then
        Mockito.verify(couponRepository).save(CouponFactory.createDefaultCoupon());
    }

    @Test
    public void addNewCouponTest_CouponAlreadyExistException() {
        //given
        DtoCouponRequest dtoCoupon = CouponFactory.createDefaultDtoCoupon();
        Mockito.when(couponRepository.existsByCouponCodeIgnoreCase(dtoCoupon.couponCode())).thenReturn(true);

        //when then
        assertThatThrownBy(() -> couponService.addNewCoupon(dtoCoupon))
                .isInstanceOf(CouponAlreadyExistsException.class)
                .hasMessageContaining("Coupon with ID: default_coupon already exist.");
    }

    @Test
    public void useCouponTestHappyPath() {
        //given
        String userId = "userId";
        Coupon coupon = CouponFactory.createDefaultCoupon();
        CouponUsage couponUsage = CouponUsageFactory.createDefaultCouponUsage(coupon);
        Mockito.when(couponRepository.findById(coupon.getCouponCode())).thenReturn(Optional.of(coupon));
        Mockito.when(couponUsageRepository.existsByUserIdAndCoupon(userId, coupon)).thenReturn(false);

        //when
        couponService.useCoupon(coupon.getCouponCode(), userId);

        //then
        coupon.setCurrentNumberOfUses(1);
        Mockito.verify(couponRepository).save(coupon);
        Mockito.verify(couponUsageRepository).save(couponUsage);
    }

    @Test
    public void useCouponTest_CouponDoesNotExistException() {
        //given
        String userId = "userId";
        Coupon coupon = CouponFactory.createDefaultCoupon();
        Mockito.when(couponRepository.findById(coupon.getCouponCode())).thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> couponService.useCoupon(coupon.getCouponCode(), userId))
                .isInstanceOf(CouponDoesNotExistException.class)
                .hasMessageContaining("Coupon with ID: default_coupon doesn't exist.");
    }

    @ParameterizedTest
    @MethodSource("provideCouponsWithExceededLimit")
    public void useCouponTest_CouponUsageLimitExceededException(Coupon coupon) {
        //given
        String userId = "userId";
        Mockito.when(couponRepository.findById(coupon.getCouponCode())).thenReturn(Optional.of(coupon));

        //when then
        assertThatThrownBy(() -> couponService.useCoupon(coupon.getCouponCode(), userId))
                .isInstanceOf(CouponUsageLimitExceededException.class)
                .hasMessageContaining("The coupon: default_coupon has already been used the maximum number of times.");
    }

    @Test
    public void useCouponTest_UserHaveAlreadyUsedCouponException() {
        //given
        String userId = "userId";
        Coupon coupon = CouponFactory.createDefaultCoupon();
        Mockito.when(couponRepository.findById(coupon.getCouponCode())).thenReturn(Optional.of(coupon));
        Mockito.when(couponUsageRepository.existsByUserIdAndCoupon(userId, coupon)).thenReturn(true);

        //when then
        assertThatThrownBy(() -> couponService.useCoupon(coupon.getCouponCode(), userId))
                .isInstanceOf(UserHasAlreadyUsedCouponException.class)
                .hasMessageContaining("User with ID: userId has already used coupon: default_coupon.");
    }

    private static Stream<Coupon> provideCouponsWithExceededLimit() {
        return Stream.of(
                CouponFactory.createCouponWithExceededLimit(),
                CouponFactory.createCouponWithEqualNumberOfUsagesAsMaxNumberOfUsages());
    }

}
