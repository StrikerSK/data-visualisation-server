package com.charts.nivo.service.graphql;

import com.charts.nivo.service.NivoCouponService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.charts.nivo.service.graphql.GraphQLService.generateParametersData;

@Component
public class PersonBarDataFetcher implements DataFetcher<List<Map<String, Object>>> {

    private final NivoCouponService couponsService;

    public PersonBarDataFetcher(NivoCouponService couponsService) {
        this.couponsService = couponsService;
    }

    @Override
    public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String grouping = dataFetchingEnvironment.getArgument("grouping");
        return couponsService.createDynamicBarData(grouping, "Person", generateParametersData(dataFetchingEnvironment));
    }

}
