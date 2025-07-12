package com.charts.nivo.service.graphql;

import com.charts.nivo.configuration.GraphCondition;
import com.charts.nivo.entity.NivoLineData;
import com.charts.nivo.service.NivoCouponService;
import com.charts.nivo.service.NivoTicketsService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Conditional(GraphCondition.class)
public class NivoLineDataFetcher implements DataFetcher<List<NivoLineData>> {

	private final NivoCouponService couponsService;
	private final NivoTicketsService ticketsService;

	@Override
	public List<NivoLineData> get(DataFetchingEnvironment dataFetchingEnvironment) {
		String kind = dataFetchingEnvironment.getArgument("kind");
		String upperGroup = dataFetchingEnvironment.getArgument("upperGroup");
		String lowerGroup = dataFetchingEnvironment.getArgument("lowerGroup");

		return switch (kind) {
			case "coupon" -> couponsService.createDynamicLineData(upperGroup, lowerGroup, GraphParameters.fetchCouponParameters(dataFetchingEnvironment));
			case "ticket" -> ticketsService.createDynamicLineData(upperGroup, lowerGroup, GraphParameters.fetchTicketParameters(dataFetchingEnvironment));
			default -> null;
		};
	}

}
