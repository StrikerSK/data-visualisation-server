package com.charts.nivo.service.graphql;

import com.charts.nivo.configuration.GraphCondition;
import com.charts.nivo.entity.NivoPieData;
import com.charts.nivo.service.NivoCouponService;
import com.charts.nivo.service.NivoTicketsService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Conditional(GraphCondition.class)
public class NivoPieDataFetcher implements DataFetcher<List<NivoPieData>> {

	private final NivoCouponService couponsService;
	private final NivoTicketsService ticketsService;

	@Override
	public List<NivoPieData> get(DataFetchingEnvironment dataFetchingEnvironment) {
		Optional<String> kind = Optional.ofNullable(dataFetchingEnvironment.getArgument("kind"));
		Optional<String> grouping = Optional.ofNullable(dataFetchingEnvironment.getArgument("grouping"));

		return switch (kind.orElse("")) {
			case "coupon" -> couponsService.createDynamicPieData(grouping.orElse("Person"), GraphParameters.generateParametersData(dataFetchingEnvironment));
			case "ticket" -> ticketsService.createDynamicPieData(grouping.orElse("Ticket"), GraphParameters.generateTicketParameters(dataFetchingEnvironment));
			default -> null;
		};
	}

}
