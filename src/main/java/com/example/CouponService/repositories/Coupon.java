package com.example.CouponService.repositories;

import com.example.CouponService.commons.Country;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
public class Coupon {

    @Id
    @Column(name = "coupon_code")
    private String couponCode;
    @Column(name = "creation_date")
    private Instant creationDate;
    @Enumerated(EnumType.STRING)
    private Country country;
    @Column(name = "max_number_of_uses")
    private Integer maxNumberOfUses;
    @Column(name = "current_number_of_uses")
    private Integer currentNumberOfUses;
    @Version
    private Integer version;

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(couponCode, coupon.couponCode) && Objects.equals(creationDate, coupon.creationDate) && country == coupon.country && Objects.equals(maxNumberOfUses, coupon.maxNumberOfUses) && Objects.equals(currentNumberOfUses, coupon.currentNumberOfUses) && Objects.equals(version, coupon.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponCode, creationDate, country, maxNumberOfUses, currentNumberOfUses, version);
    }
}
