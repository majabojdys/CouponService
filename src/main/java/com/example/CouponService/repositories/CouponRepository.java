package com.example.CouponService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, String> {

    boolean existsByCouponCodeIgnoreCase(String couponCode);

}
