package com.charts.nivo.service.graphql;

import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.nivo.service.NivoCouponService;
import com.charts.nivo.utils.GraphFetcherUtil;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SellTypeBarDataFetcher implements DataFetcher<List<Map<String, Object>>> {

    private final NivoCouponService couponsService;

    public SellTypeBarDataFetcher(NivoCouponService couponsService) {
        this.couponsService = couponsService;
    }

    @Override
    public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String upperGroup = dataFetchingEnvironment.getArgument("upperGroup");
        List<Map<String, Object>> output = couponsService.createDynamicBarData(upperGroup, "Type", GraphParameters.generateParametersData(dataFetchingEnvironment));
        return GraphFetcherUtil.fetchValue(output, SellType.class);
    }

}
