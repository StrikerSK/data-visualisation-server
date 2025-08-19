package com.charts.nivo.service.graphql;

import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.api.coupon.entity.enums.types.Validity;
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
public class ValidityBarDataFetcher implements DataFetcher<List<Map<String, Object>>> {

    private final NivoCouponService couponsService;

    @Override
    public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String lowerGroup = "Validity";
        String upperGroup = dataFetchingEnvironment.getArgument("upperGroup");
        CouponsParameters parameters = GraphParameters.fetchCouponParameters(dataFetchingEnvironment);

        List<Map<String, Object>> output = couponsService.createDynamicBarData(upperGroup, lowerGroup, parameters);
        return GraphFetcherUtil.fetchValue(output, Validity.class);
    }

}
