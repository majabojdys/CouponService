package com.example.CouponService;

import com.example.CouponService.dtos.DtoCouponRequest;
import com.example.CouponService.exceptions.CouponAlreadyExistException;
import com.example.CouponService.factories.ClockFactory;
import com.example.CouponService.factories.CouponFactory;
import com.example.CouponService.repositories.CouponRepository;
import org.junit.Test;
import org.mockito.Mockito;
import java.time.Clock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class CouponServiceUnitTest {

    private final CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
    private final Clock clock = ClockFactory.getFixedClock();

    private final CouponService couponService = new CouponService(couponRepository, clock);

    @Test
    public void addNewCouponTestHappyPath(){
        //given
        DtoCouponRequest dtoCoupon = CouponFactory.createDefaultDtoCoupon();
        Mockito.when(couponRepository.existsByCouponCodeIgnoreCase(dtoCoupon.couponCode())).thenReturn(false);

        //when
        couponService.addNewCoupon(dtoCoupon);

        //then
        Mockito.verify(couponRepository).save(CouponFactory.createDefaultCoupon());
    }

    @Test
    public void addNewCouponTestExceptionPath(){
        //given
        DtoCouponRequest dtoCoupon = CouponFactory.createDefaultDtoCoupon();
        Mockito.when(couponRepository.existsByCouponCodeIgnoreCase(dtoCoupon.couponCode())).thenReturn(true);

        //when then
        assertThatThrownBy(() -> couponService.addNewCoupon(dtoCoupon))
                .isInstanceOf(CouponAlreadyExistException.class)
                .hasMessageContaining("Coupon with ID: default coupon already exist.");
    }

}
