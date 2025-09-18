package com.example.CouponService.services;

import com.example.CouponService.clients.IpCheckerClient;
import com.example.CouponService.exceptions.CouponDoesNotExistException;
import com.example.CouponService.exceptions.ForbiddenCountryException;
import com.example.CouponService.repositories.Coupon;
import com.example.CouponService.repositories.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class IpCheckerService {

    private final IpCheckerClient ipCheckerClient;
    private final CouponRepository couponRepository;

    public IpCheckerService(IpCheckerClient ipCheckerClient, CouponRepository couponRepository) {
        this.ipCheckerClient = ipCheckerClient;
        this.couponRepository = couponRepository;
    }

    public void validateCouponCountryWithUserIp(String ip, String couponCode){
        Coupon coupon = couponRepository.findById(couponCode).orElseThrow(() -> new CouponDoesNotExistException(couponCode));
        String country = ipCheckerClient.getCountryByIp(ip);
        if(!country.equals(coupon.getCountry().getCountry())){
            throw new ForbiddenCountryException(couponCode, country);
        }
    }
}
