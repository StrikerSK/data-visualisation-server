package com.charts.nivo.utils;

import com.charts.general.entity.GroupingEntity;
import com.charts.general.entity.AbstractUpdateEntity;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.utils.AbstractGroupingUtils;
import com.charts.nivo.entity.NivoBubbleData;
import com.charts.nivo.entity.NivoDataXY;
import com.charts.nivo.entity.NivoLineData;
import com.charts.nivo.entity.NivoPieData;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NivoConvertersUtils {

    /**
     *
     * @param groupingList List of grouped entities retrieved from database
     * @return List of processed data into Nivo Pie data structure
     * @param <T> Application defined enumeration implementing {@link IEnum}
     */
    public static <T extends IEnum> List<NivoPieData> createPieData(List<GroupingEntity<T>> groupingList) {
        groupingList.sort(Comparator.comparingInt(e -> e.getKey().getOrderValue()));
        return groupingList
                .stream()
                .map(e -> new NivoPieData(e.getKey().getValue(), e.getKey().getOrderValue(), e.getValue().intValue()))
                .collect(Collectors.toList());
    }

    /**
     * Method gets data for displaying bar chart. It is divided by two grouping upper grouping and nested grouping.
     *
     * @param input all entries obtained from database
     * @param upperGrouping top level grouping of data
     * @param nestedGrouping nested grouping within upper grouping
     */
    public static <T extends IEnum, R extends IEnum, E extends AbstractUpdateEntity> List<Map<String, Object>> createBarData(
            List<E> input,
            Function<List<E>, Map<T, List<E>>> upperGrouping,
            Function<List<E>, Map<R, List<E>>> nestedGrouping
    ) {
        return upperGrouping.apply(input).entrySet()
                .stream()
                .map(upper -> {
                    Map<String, Object> outputMap = nestedGrouping.apply(upper.getValue())
                            .entrySet()
                            .stream()
                            .map(lower -> new AbstractMap.SimpleEntry<>(lower.getKey().getValue(), AbstractGroupingUtils.aggregateGroupSum(lower.getValue())))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (e1, e2) -> e1,
                                    LinkedHashMap::new
                            ));
                    outputMap.put("label", upper.getKey().getValue());
                    return outputMap;
                })
                .collect(Collectors.toList());
    }

    /**
     * Method creates two level grouping. Level one grouping contains id and list of data that is composed by level two grouping.
     *
     * @param input all entries obtained from database
     * @param upperGrouping top level grouping of data that which key value is used as 'id' parameter
     * @param nestedGrouping grouping of listed data in 'data' parameter
     * @param aggregator aggregation function
     */
    public static <T extends IEnum, R extends IEnum, E extends AbstractUpdateEntity> List<NivoLineData> createLineData(
            List<E> input,
            Function<List<E>, Map<T, List<E>>> upperGrouping,
            Function<List<E>, Map<R, List<E>>> nestedGrouping,
            Function<List<E>, Integer> aggregator
    ) {
        return upperGrouping.apply(input).entrySet()
                .stream()
                .map(e -> calculateLineData(e, nestedGrouping, aggregator))
                .collect(Collectors.toList());
    }

    private static  <T extends IEnum, R extends IEnum, E extends AbstractUpdateEntity> NivoLineData calculateLineData(
            Map.Entry<T, List<E>> mapEntry,
            Function<List<E>, Map<R, List<E>>> nestedGrouping,
            Function<List<E>, Integer> aggregator
    ) {
        List<NivoDataXY> summarizedGroups = nestedGrouping.apply(mapEntry.getValue()).entrySet()
                .stream()
                .map(entry -> new NivoDataXY(entry.getKey(), aggregator.apply(entry.getValue()).longValue()))
                .collect(Collectors.toList());
        return new NivoLineData(mapEntry.getKey(), summarizedGroups);
    }

    /**
     * Method gets data for displaying bubble chart. It is divided by two grouping upper grouping and nested grouping.
     *
     * @param input all entries obtained from database
     * @param upperGrouping top level grouping of data
     * @param lowerGrouping nested grouping of withing upper grouping
     * @param aggregator aggregation function
     */
    public static <T extends IEnum, R extends IEnum, E extends AbstractUpdateEntity> NivoBubbleData createBubbleData(
            List<E> input,
            Function<List<E>, Map<T, List<E>>> upperGrouping,
            Function<List<E>, Map<R, List<E>>> lowerGrouping,
            Function<List<E>, Integer> aggregator
    ) {
        List<NivoBubbleData> middleNivoBubbleDataList = new ArrayList<>();
        upperGrouping.apply(input).forEach((key, entity) -> {
            List<NivoBubbleData> nestedNivoBubbleDataList = lowerGrouping.apply(entity)
                    .entrySet()
                    .stream()
                    .map(e -> new NivoBubbleData(e.getKey(), aggregator.apply(e.getValue())))
                    .collect(Collectors.toList());
            middleNivoBubbleDataList.add(new NivoBubbleData(key, nestedNivoBubbleDataList));
        });
        middleNivoBubbleDataList.sort(Comparator.comparingInt(NivoBubbleData::getOrderValue));
        return new NivoBubbleData("PID predaj", middleNivoBubbleDataList);
    }

}
