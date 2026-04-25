package com.charts.general.utils;

import com.charts.general.entity.AbstractUpdateEntity;
import com.charts.general.entity.enums.types.EnumAdapter;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.entity.enums.types.Months;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.charts.general.entity.constants.EnumerationConstants.MONTH_LIST;

public final class AbstractGroupingUtils {

    private AbstractGroupingUtils() {
    }

    public static <T extends AbstractUpdateEntity> Map<EnumAdapter, List<T>> groupByYear(List<T> couponEntityList) {
        return groupValues(couponEntityList, e -> new EnumAdapter(e.getYear()));
    }

    public static <T extends AbstractUpdateEntity> Map<Months, List<T>> groupByMonth(List<T> entityList) {
        return groupValues(entityList, T::getMonth, MONTH_LIST);
    }

    public static <T extends IEnum, V> Map<String, V> convertMapKeysToString(Map<T, V> map) {
        return map.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey().getValue(),
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
        ));
    }

    /**
     * Method aggregates all groups by sum of values
     * @param entityList Map of values that are supposed to be groups per enumeration implementing {@link IEnum}
     * @return Map of values that are supposed to be groups per enumeration implementing {@link IEnum}
     *
     * @param <T> Enumeration implementing {@link IEnum}
     * @param <R> Values that are implementing implementing {@link AbstractUpdateEntity}
     */
    public static <T extends IEnum, R extends AbstractUpdateEntity> Map<T, Long> aggregateGroupsSum(Map<T, List<R>> entityList) {
        return entityList.entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), aggregateGroupSum(e.getValue()).longValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * Method groups and sums all value present
     * @param entryList Map of values that are supposed to be groups per enumeration implementing {@link IEnum}
     * @param groupingFunction Function that would be used to group values
     * @return Map of values that are supposed to be groups per enumeration implementing {@link IEnum}
     *
     * @param <T> Enumeration implementing {@link IEnum}
     * @param <R> Values that are implementing implementing {@link AbstractUpdateEntity}
     */
    public static <T extends IEnum, R extends AbstractUpdateEntity> Map<T, Long> aggregateGroupsSum(List<R> entryList, Function<List<R>, Map<T, List<R>>> groupingFunction) {
        Map<T, List<R>> groupedValues = groupingFunction.apply(entryList);
        return aggregateGroupsSum(groupedValues);
    }

    /**
     * Method sums all values in list
     * @param entityList List of values
     * @return Sum of values
     * @param <T> Type of value that will be implementing {@link AbstractUpdateEntity}
     */
    public static <T extends AbstractUpdateEntity> Integer aggregateGroupSum(List<T> entityList) {
        return entityList.stream().mapToInt(T::getValue).sum();
    }

    /**
     * Method creates groups of values based on provided grouping function
     * @param entryList Simple list of value that are supposed to be grouped
     * @param groupingFunction Function that would be used to group values
     *
     * @return Map of values that are supposed to be groups per enumeration implementing {@link IEnum} IEnum interface
     * @param <T> Enumeration implementing {@link IEnum}
     * @param <R> Type of value that will be implementing {@link AbstractUpdateEntity}
     */
    public static <T extends IEnum, R> Map<T, List<R>> groupValues(List<R> entryList, Function<R, T> groupingFunction) {
        return groupValues(entryList, groupingFunction, null);
    }

    /**
     * Method creates groups of values based on provided grouping function
     * @param entryList Simple list of value that are supposed to be grouped
     * @param groupingFunction Function that would be used to group values
     * @param values List of values that are supposed to be filled in case of missing
     * @return Map of values that are supposed to be groups per enumeration implementing {@link IEnum} IEnum interface
     * @param <T> Enumeration implementing {@link IEnum}
     * @param <R> Type of value that will be implementing {@link AbstractUpdateEntity}
     */
    public static <T extends IEnum, R> Map<T, List<R>> groupValues(List<R> entryList, Function<R, T> groupingFunction, List<T> values) {
        Map<T, List<R>> map = entryList
                .stream()
                .collect(Collectors.groupingBy(groupingFunction));

        if (values != null){
            values.forEach(v -> map.putIfAbsent(v, Collections.emptyList()));
        }

        return AbstractSortingUtils.sortByOrderValue(map);
    }

}
