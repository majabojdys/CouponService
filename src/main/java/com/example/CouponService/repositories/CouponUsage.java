package com.example.CouponService.repositories;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(
        name = "coupon_usages",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "coupon_code"})
)
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "used_at", nullable = false)
    private Instant usedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_code",
            referencedColumnName = "coupon_code",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_coupon_usage_coupon")
    )
    private Coupon coupon;

    public CouponUsage() {
    }

    public CouponUsage(String userId, Instant usedAt, Coupon coupon) {
        this.userId = userId;
        this.usedAt = usedAt;
        this.coupon = coupon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(Instant usedAt) {
        this.usedAt = usedAt;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CouponUsage that = (CouponUsage) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(usedAt, that.usedAt) && Objects.equals(coupon, that.coupon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, usedAt, coupon);
    }
}
