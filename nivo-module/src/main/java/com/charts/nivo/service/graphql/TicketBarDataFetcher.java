package com.charts.nivo.service.graphql;

import com.charts.api.ticket.entity.TicketsParameters;
import com.charts.api.ticket.entity.enums.TicketType;
import com.charts.nivo.configuration.GraphCondition;
import com.charts.nivo.service.NivoTicketsService;
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
public class TicketBarDataFetcher implements DataFetcher<List<Map<String, Object>>> {

    private final NivoTicketsService ticketsService;

    @Override
    public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String lowerGroup = "Ticket";
        String upperGroup = dataFetchingEnvironment.getArgument("upperGroup");
        TicketsParameters parameters = GraphParameters.fetchTicketParameters(dataFetchingEnvironment);

        List<Map<String, Object>> output = ticketsService.createDynamicBarData(upperGroup, lowerGroup, parameters);
        return GraphFetcherUtil.fetchValue(output, TicketType.class);
    }

}
