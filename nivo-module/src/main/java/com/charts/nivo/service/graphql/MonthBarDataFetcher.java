package com.charts.nivo.service.graphql;

import com.charts.general.entity.enums.EnumUtils;
import com.charts.general.entity.enums.types.Months;
import com.charts.nivo.service.NivoCouponService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.charts.nivo.service.graphql.GraphQLService.generateParametersData;

@Component
public class MonthBarDataFetcher implements DataFetcher<List<Map<String, Object>>> {

    private final NivoCouponService couponsService;

    public MonthBarDataFetcher(NivoCouponService couponsService) {
        this.couponsService = couponsService;
    }

    @Override
    public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String upperGroup = dataFetchingEnvironment.getArgument("upperGroup");
        List<Map<String, Object>> output = couponsService.createDynamicBarData(upperGroup, "Month", generateParametersData(dataFetchingEnvironment));
        return output.stream()
                .map(originalMap -> {
                    Map<String, Object> newMap = new HashMap<>();
                    for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
                        String originalKey = entry.getKey();
                        if (!originalKey.equals("label")) {
                            Object value = entry.getValue();
                            String modifiedKey = EnumUtils.getSystemValue(Months.class, originalKey);
                            newMap.put(modifiedKey, value);
                        }
                    }
                    return newMap;
                })
                .collect(Collectors.toList());
    }

}
