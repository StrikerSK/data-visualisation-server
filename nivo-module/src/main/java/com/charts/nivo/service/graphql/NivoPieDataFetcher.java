package com.charts.nivo.service.graphql;

import com.charts.nivo.entity.NivoPieData;
import com.charts.nivo.service.NivoCouponService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class NivoPieDataFetcher implements DataFetcher<List<NivoPieData>> {

	private final NivoCouponService couponsService;

	public NivoPieDataFetcher(NivoCouponService couponsService) {
		this.couponsService = couponsService;
	}

	@Override
	public List<NivoPieData> get(DataFetchingEnvironment dataFetchingEnvironment) {
		Optional<String> grouping = Optional.ofNullable(dataFetchingEnvironment.getArgument("grouping"));
		return couponsService.createDynamicPieData(grouping.orElse("Person"), GraphParameters.generateParametersData(dataFetchingEnvironment));
	}

}
