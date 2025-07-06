package com.charts.api.coupon.repository;

import com.charts.api.coupon.entity.v1.CouponEntityV1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public interface JpaCouponRepository extends JpaRepository<CouponEntityV1, Long> {
}
