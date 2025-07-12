package com.charts.nivo.service.graphql;

import com.charts.general.entity.enums.types.Months;
import com.charts.nivo.configuration.GraphCondition;
import com.charts.nivo.service.NivoTicketsService;
import com.charts.nivo.utils.GraphFetcherUtil;
import com.charts.nivo.service.NivoCouponService;
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
public class MonthBarDataFetcher implements DataFetcher<List<Map<String, Object>>> {

    private final NivoCouponService couponsService;
    private final NivoTicketsService ticketsService;

    @Override
    public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String kind = dataFetchingEnvironment.getArgument("kind");
        String upperGroup = dataFetchingEnvironment.getArgument("upperGroup");

        List<Map<String, Object>> output = switch(kind) {
            case "coupon" -> couponsService.createDynamicBarData(upperGroup, "Month", GraphParameters.fetchCouponParameters(dataFetchingEnvironment));
            case "ticket" ->  ticketsService.createDynamicBarData(upperGroup, "Month", GraphParameters.fetchTicketParameters(dataFetchingEnvironment));
            default -> List.of();
        };
        return GraphFetcherUtil.fetchValue(output, Months.class);
    }

}
