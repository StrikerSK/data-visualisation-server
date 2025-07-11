package com.charts.nivo.service.graphql;

import com.charts.nivo.entity.NivoPieData;
import com.charts.nivo.service.NivoCouponService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.charts.nivo.service.graphql.GraphQLService.generateParametersData;

@Component
public class NivoPieDataFetcher implements DataFetcher<List<NivoPieData>> {

	private final NivoCouponService couponsService;

	public NivoPieDataFetcher(NivoCouponService couponsService) {
		this.couponsService = couponsService;
	}

	@Override
	public List<NivoPieData> get(DataFetchingEnvironment dataFetchingEnvironment) {
		return couponsService.createDynamicPieData("year", generateParametersData(dataFetchingEnvironment));
	}

}
