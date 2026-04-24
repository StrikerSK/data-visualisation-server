package com.charts.general.utils;

import com.charts.general.entity.GroupingEntity;
import com.charts.general.entity.enums.types.EnumAdapter;

import java.util.List;

public final class GroupingEntityUtils {

    private GroupingEntityUtils() {
    }

    public static List<GroupingEntity<EnumAdapter>> fromIntegers(List<GroupingEntity<Integer>> values) {
        return values.stream()
                .map(entry -> new GroupingEntity<>(new EnumAdapter(entry.getKey()), entry.getValue()))
                .toList();
    }

    public static List<GroupingEntity<EnumAdapter>> fromBooleans(List<GroupingEntity<Boolean>> values) {
        return values.stream()
                .map(entry -> new GroupingEntity<>(new EnumAdapter(entry.getKey()), entry.getValue()))
                .toList();
    }
}
