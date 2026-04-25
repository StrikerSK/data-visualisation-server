package com.charts.api.coupon.utils;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.entity.enums.types.PersonType;
import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.api.coupon.entity.enums.types.Validity;
import com.charts.general.utils.AbstractFilterUtils;
import com.charts.general.entity.enums.types.Months;

import java.util.List;

public final class CouponFilterUtils {

    private CouponFilterUtils() {
    }

    public static List<UpdateCouponEntity> filterByMonth(List<UpdateCouponEntity> entries, List<Months> values) {
        return AbstractFilterUtils.filterByMonth(entries, values);
    }

    public static List<UpdateCouponEntity> filterByYear(List<UpdateCouponEntity> entries, List<Integer> values) {
        return AbstractFilterUtils.filterByYear(entries, values);
    }

    public static List<UpdateCouponEntity> filterBySellType(List<UpdateCouponEntity> entries, List<SellType> values) {
        return AbstractFilterUtils.filterByValue(entries, values, UpdateCouponEntity::getSellType);
    }

    public static List<UpdateCouponEntity> filterByValidity(List<UpdateCouponEntity> entries, List<Validity> values) {
        return AbstractFilterUtils.filterByValue(entries, values, UpdateCouponEntity::getValidity);
    }

    public static List<UpdateCouponEntity> filterByPersonType(List<UpdateCouponEntity> entries, List<PersonType> values) {
        return AbstractFilterUtils.filterByValue(entries, values, UpdateCouponEntity::getPersonType);
    }

}
