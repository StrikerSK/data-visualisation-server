package com.charts.api.coupon.utils;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.exception.InvalidParameterException;
import com.charts.general.utils.AbstractFunctionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class CouponFunctionUtils {

    public static final String PERSON_GROUP = "person";
    public static final String SELL_GROUP = "sell";
    public static final String VALIDITY_GROUP = "validity";
    public static final String MONTH_GROUP = AbstractFunctionUtils.MONTH_GROUP;
    public static final String YEAR_GROUP = AbstractFunctionUtils.YEAR_GROUP;

    private CouponFunctionUtils() {
    }

    public static void validateGroups(String upperGroup, String lowerGroup) {
        AbstractFunctionUtils.validateGroups(upperGroup, lowerGroup);
    }

    public static Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> createGrouping(String groupName) {
        Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> convertedData;

        switch (groupName.toLowerCase()) {
            case PERSON_GROUP:
                convertedData = groupBy(CouponGroupingUtils::groupByPersonType);
                break;
            case MONTH_GROUP:
                convertedData = groupBy(CouponGroupingUtils::groupByMonth);
                break;
            case SELL_GROUP:
                convertedData = groupBy(CouponGroupingUtils::groupBySellType);
                break;
            case VALIDITY_GROUP:
                convertedData = groupBy(CouponGroupingUtils::groupByValidity);
                break;
            case YEAR_GROUP:
                convertedData = groupBy(CouponGroupingUtils::groupByYear);
                break;
            default:
                throw new InvalidParameterException(
                        String.format("Unknown group name: %s! Available groups: %s, %s, %s, %s, %s", groupName, YEAR_GROUP, MONTH_GROUP, SELL_GROUP, VALIDITY_GROUP, PERSON_GROUP)
                );
        }

        return convertedData;
    }

    private static <T extends IEnum> Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> groupBy(
            Function<List<UpdateCouponEntity>, Map<T, List<UpdateCouponEntity>>> groupingFunction
    ) {
        return entries -> new LinkedHashMap<>(groupingFunction.apply(entries));
    }

}
