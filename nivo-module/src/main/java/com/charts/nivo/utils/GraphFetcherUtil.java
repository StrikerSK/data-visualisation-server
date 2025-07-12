package com.charts.nivo.utils;

import com.charts.general.entity.enums.EnumUtils;
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
                            String systemValue = EnumUtils.getSystemValue(clazz, originalKey);
                            newMap.put(systemValue, entry.getValue());
                        }
                    }
                    return newMap;
                })
                .collect(Collectors.toList());
    }

}
