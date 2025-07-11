package com.charts.nivo.service.graphql;

import com.charts.nivo.entity.NivoBubbleData;
import com.charts.nivo.service.NivoCouponService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import static com.charts.nivo.service.graphql.GraphQLService.generateParametersData;

@Component
public class NivoBarDataFetcher implements DataFetcher<NivoBubbleData> {

	private final NivoCouponService couponsService;

	public NivoBarDataFetcher(NivoCouponService couponsService) {
		this.couponsService = couponsService;
	}

	@Override
	public NivoBubbleData get(DataFetchingEnvironment dataFetchingEnvironment) {
		String upperGroup = dataFetchingEnvironment.getArgument("upperGroup");
		String lowerGroup = dataFetchingEnvironment.getArgument("lowerGroup");
		return couponsService.createDynamicBubbleData(upperGroup, lowerGroup, generateParametersData(dataFetchingEnvironment));
	}

	private final DataFetcher<Object> nivoDataFetcher = environment -> {
		String type = environment.getArgument("type");
		switch (type) {
			case "bar":
				return nivoBarDataFetcher.get(environment);  // return NivoBarData instance
			case "line":
				return nivoLineDataFetcher.get(environment); // return NivoLineData instance
			case "pie":
				return nivoPieDataFetcher.get(environment);  // return NivoPieData instance
			default:
				return null;
		}
	};
}
