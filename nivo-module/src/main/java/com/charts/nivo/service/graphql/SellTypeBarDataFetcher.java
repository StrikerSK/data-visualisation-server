package com.charts.nivo.service.graphql;

import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.nivo.configuration.GraphCondition;
import com.charts.nivo.service.NivoCouponService;
import com.charts.nivo.utils.GraphFetcherUtil;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Conditional(GraphCondition.class)
public class SellTypeBarDataFetcher implements DataFetcher<List<Map<String, Object>>> {

    private final NivoCouponService couponsService;

    @Override
    public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String upperGroup = dataFetchingEnvironment.getArgument("upperGroup");
        List<Map<String, Object>> output = couponsService.createDynamicBarData(upperGroup, "Type", GraphParameters.fetchCouponParameters(dataFetchingEnvironment));
        return GraphFetcherUtil.fetchValue(output, SellType.class);
    }

}
