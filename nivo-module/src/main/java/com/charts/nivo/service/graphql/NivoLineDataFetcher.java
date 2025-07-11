package com.charts.nivo.service.graphql;

import com.charts.nivo.entity.NivoLineData;
import com.charts.nivo.service.NivoCouponService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.charts.nivo.service.graphql.GraphQLService.generateParametersData;

@Component
public class NivoLineDataFetcher implements DataFetcher<List<NivoLineData>> {

	private final NivoCouponService couponsService;

	public NivoLineDataFetcher(NivoCouponService couponsService) {
		this.couponsService = couponsService;
	}

	@Override
	public List<NivoLineData> get(DataFetchingEnvironment dataFetchingEnvironment) {
		return couponsService.createDynamicLineData("year", "month", generateParametersData(dataFetchingEnvironment));
	}

}
