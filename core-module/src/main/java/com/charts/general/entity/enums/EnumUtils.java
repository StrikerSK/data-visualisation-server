package com.charts.general.entity.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnumUtils {

    public static <T extends IEnum> List<T> getValueList(Class<T> clazz) {
        if (clazz == null || !clazz.isEnum()) {
            return null;
        }
        return Arrays.asList(clazz.getEnumConstants());
    }

    public static <T extends IEnum> List<T> getValueList(List<String> searchedValues, Class<T> clazz) {
        return getValueList(clazz).stream()
                .filter(v -> searchedValues.contains(v.getValue()))
                .collect(Collectors.toList());
    }

    public static <T extends IEnum> Optional<T> getValue(Class<T> clazz, String label) {
        return getValueList(clazz).stream()
                .filter(c -> c.getValue().equals(label))
                .findFirst();
    }

    public static <T extends IEnum> T fromValue(Class<T> clazz, String label) {
        if (clazz == null || !clazz.isEnum()) {
            throw new IllegalArgumentException("Class must not be null and an enum");
        }

        return EnumUtils.getValue(clazz, label).orElseThrow(() -> new IllegalArgumentException("Unknown label " + label));
    }

    public static <T extends IEnum> String getSystemValue(Class<T> clazz, String label) {
        return fromValue(clazz, label).getSystemValue();
    }

    public static <T extends IEnum> List<String> getStringValues(Class<T> clazz) {
        return getValueList(clazz).stream()
                .map(IEnum::getValue)
                .collect(Collectors.toList());
    }

    public static <T extends IEnum> List<String> getSystemValues(Class<T> clazz) {
        return getValueList(clazz).stream()
                .map(IEnum::getSystemValue)
                .collect(Collectors.toList());
    }

}
