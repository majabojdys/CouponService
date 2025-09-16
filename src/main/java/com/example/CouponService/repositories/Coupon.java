package com.example.CouponService.repositories;

import com.example.CouponService.commons.Country;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Coupon {

    @Id
    private String couponCode;
    private Instant creationDate;
    @Enumerated(EnumType.STRING)
    private Country country;
    private Integer maxNumberOfUses;
    private Integer currentNumberOfUses;


    public Coupon() {
    }

    public Coupon(String couponCode, Instant creationDate,  Country country, Integer maxNumberOfUses) {
        this.couponCode = couponCode;
        this.creationDate = creationDate;
        this.country = country;
        this.maxNumberOfUses = maxNumberOfUses;
        this.currentNumberOfUses = 0;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getMaxNumberOfUses() {
        return maxNumberOfUses;
    }

    public void setMaxNumberOfUses(Integer maxNumberOfUses) {
        this.maxNumberOfUses = maxNumberOfUses;
    }

    public Integer getCurrentNumberOfUses() {
        return currentNumberOfUses;
    }

    public void setCurrentNumberOfUses(Integer currentNumberOfUses) {
        this.currentNumberOfUses = currentNumberOfUses;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(couponCode, coupon.couponCode) && Objects.equals(creationDate, coupon.creationDate) && country == coupon.country && Objects.equals(maxNumberOfUses, coupon.maxNumberOfUses) && Objects.equals(currentNumberOfUses, coupon.currentNumberOfUses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponCode, creationDate, country, maxNumberOfUses, currentNumberOfUses);
    }
}
