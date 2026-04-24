package com.charts.nivo.utils;

import com.charts.general.entity.enums.IEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphFetcherUtil {

    public static <T extends IEnum> List<Map<String, Object>> fetchValue(List<Map<String, Object>> input, Class<T> clazz) {
        return input.stream()
                .map(originalMap -> {
                    Map<String, Object> newMap = new HashMap<>();
                    for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
                        String originalKey = entry.getKey();
                        if (!"label".equals(originalKey)) {
                            String systemValue = normalizeSchemaField(getSystemValue(clazz, originalKey));
                            newMap.put(systemValue, entry.getValue());
                        } else {
                            newMap.put(originalKey, entry.getValue());
                        }
                    }
                    return newMap;
                })
                .collect(Collectors.toList());
    }

    private static <T extends IEnum> String getSystemValue(Class<T> clazz, String label) {
        for (T enumValue : clazz.getEnumConstants()) {
            if (readEnumProperty(enumValue, "getValue").equals(label)) {
                return readEnumProperty(enumValue, "getSystemValue");
            }
        }
        throw new IllegalArgumentException("Unknown label " + label + " for enum " + clazz.getSimpleName());
    }

    private static String readEnumProperty(Object enumValue, String methodName) {
        try {
            return String.valueOf(enumValue.getClass().getMethod(methodName).invoke(enumValue));
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to read enum property via " + methodName, e);
        }
    }

    private static String normalizeSchemaField(String systemValue) {
        if ("portables".equals(systemValue)) {
            return "portable";
        }
        return systemValue;
    }

}
