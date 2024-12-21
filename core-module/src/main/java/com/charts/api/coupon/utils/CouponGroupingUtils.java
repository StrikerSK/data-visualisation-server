package com.charts.api.coupon.utils;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.general.entity.AbstractUpdateEntity;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.entity.enums.Months;
import com.charts.general.entity.enums.PersonType;
import com.charts.general.entity.enums.SellType;
import com.charts.general.entity.enums.Validity;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CouponGroupingUtils {

    public static Map<PersonType, List<UpdateCouponEntity>> groupByPersonType(List<UpdateCouponEntity> couponEntityList) {
        return couponEntityList
                .stream()
                .collect(Collectors.groupingBy(UpdateCouponEntity::getPersonType));
    }

    public static Map<Months, List<UpdateCouponEntity>> groupByMonth(List<UpdateCouponEntity> couponEntityList) {
        return couponEntityList
                .stream()
                .collect(Collectors.groupingBy(UpdateCouponEntity::getMonth));
    }

    public static Map<Validity, List<UpdateCouponEntity>> groupByValidity(List<UpdateCouponEntity> couponEntityList) {
        return couponEntityList
                .stream()
                .collect(Collectors.groupingBy(UpdateCouponEntity::getValidity));
    }

    public static Map<SellType, List<UpdateCouponEntity>> groupBySellType(List<UpdateCouponEntity> couponEntityList) {
        return couponEntityList
                .stream()
                .collect(Collectors.groupingBy(UpdateCouponEntity::getSellType));
    }

    public static <T extends AbstractUpdateEntity> Map<Months, Object> groupAndSumByMonth(List<T> entityList) {
        return sortByOrderValue(new HashMap<>(entityList.stream().collect(Collectors.groupingBy(T::getMonth, Collectors.summingInt(T::getValue)))));
    }

    protected static <R extends IEnum, T> Map<R, T> sortByOrderValue(Map<R, T> mapToSort) {
        return mapToSort.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparingInt(R::getOrderValue)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static <T extends IEnum> Map<T, Long> sumGroup(Map<T, List<UpdateCouponEntity>> entityList) {
        return entityList.entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), sumGroup(e.getValue())))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

    }

    public static Long sumGroup(List<UpdateCouponEntity> entityList) {
        return (long) entityList.stream().mapToInt(UpdateCouponEntity::getValue).sum();
    }

}