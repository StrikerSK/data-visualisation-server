package com.charts.api.coupon.repository;

import com.charts.api.coupon.entity.v1.CouponEntityV1;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.utils.CouponConvertor;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Deprecated
public class CouponRepository {

    private final JpaCouponRepository couponRepository;

    public List<CouponEntityV1> findAll() { return couponRepository.findAll(); }

    @Cacheable("couponList")
    public List<UpdateCouponEntity> getUpdateCouponList() {
        return CouponConvertor.convertCouponEntity(findAll());
    }

}
