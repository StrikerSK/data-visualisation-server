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
}
