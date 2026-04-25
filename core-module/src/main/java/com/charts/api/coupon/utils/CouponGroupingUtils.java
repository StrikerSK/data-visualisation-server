package com.charts.api.coupon.utils;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.entity.enums.types.PersonType;
import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.api.coupon.entity.enums.types.Validity;
import com.charts.general.entity.enums.types.EnumAdapter;
import com.charts.general.entity.enums.types.Months;
import com.charts.general.utils.AbstractGroupingUtils;

import java.util.List;
import java.util.Map;

public final class CouponGroupingUtils {

    private CouponGroupingUtils() {
    }

    public static Map<PersonType, List<UpdateCouponEntity>> groupByPersonType(List<UpdateCouponEntity> entries) {
        return AbstractGroupingUtils.groupValues(entries, UpdateCouponEntity::getPersonType);
    }

    public static Map<Validity, List<UpdateCouponEntity>> groupByValidity(List<UpdateCouponEntity> entries) {
        return AbstractGroupingUtils.groupValues(entries, UpdateCouponEntity::getValidity);
    }

    public static Map<SellType, List<UpdateCouponEntity>> groupBySellType(List<UpdateCouponEntity> entries) {
        return AbstractGroupingUtils.groupValues(entries, UpdateCouponEntity::getSellType);
    }

    public static Map<Months, List<UpdateCouponEntity>> groupByMonth(List<UpdateCouponEntity> entries) {
        return AbstractGroupingUtils.groupByMonth(entries);
    }

    public static Map<EnumAdapter, List<UpdateCouponEntity>> groupByYear(List<UpdateCouponEntity> entries) {
        return AbstractGroupingUtils.groupByYear(entries);
    }

    public static Integer aggregateGroupSum(List<UpdateCouponEntity> entries) {
        return AbstractGroupingUtils.aggregateGroupSum(entries);
    }

}
