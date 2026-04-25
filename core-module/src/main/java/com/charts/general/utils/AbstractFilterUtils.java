package com.charts.general.utils;

import com.charts.general.entity.AbstractUpdateEntity;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.entity.enums.types.Months;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AbstractFilterUtils {

    private AbstractFilterUtils() {
    }

    public static <T extends AbstractUpdateEntity> List<T> filterByMonth(List<T> entries, List<Months> values) {
        return filterByValue(entries, values, T::getMonth);
    }

    public static <T extends AbstractUpdateEntity> List<T> filterByYear(List<T> entries, List<Integer> values) {
        return filterByNonEnum(entries, values, T::getYear);
    }

    public static <T, R> List<R> filterByNonEnum(List<R> input, List<T> values, Function<R, T> function) {
        return abstractFilter(input, values, function);
    }

    public static <T extends IEnum, R> List<R> filterByValue(List<R> input, List<T> values, Function<R, T> function) {
        return abstractFilter(input, values, function);
    }

    private static <T, R> List<R> abstractFilter(List<R> input, List<T> values, Function<R, T> filterFunction) {
        return input.stream()
                .filter(coupon -> values.contains(filterFunction.apply(coupon)))
                .collect(Collectors.toList());
    }

}
