package com.charts.api.coupon.utils;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.entity.enums.types.PersonType;
import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.api.coupon.entity.enums.types.Validity;
import com.charts.general.utils.AbstractGroupingUtils;

import java.util.List;
import java.util.Map;

public class CouponGroupingUtils extends AbstractGroupingUtils {

    public static Map<PersonType, List<UpdateCouponEntity>> groupByPersonType(List<UpdateCouponEntity> couponEntityList) {
        return groupValues(couponEntityList, UpdateCouponEntity::getPersonType);
    }

    public static Map<Validity, List<UpdateCouponEntity>> groupByValidity(List<UpdateCouponEntity> couponEntityList) {
        return groupValues(couponEntityList, UpdateCouponEntity::getValidity);
    }

    public static Map<SellType, List<UpdateCouponEntity>> groupBySellType(List<UpdateCouponEntity> couponEntityList) {
        return groupValues(couponEntityList, UpdateCouponEntity::getSellType);
    }

}
