package com.javapid.service.graphql;

import com.javapid.entity.nivo.bar.NivoBarDataByMonth;
import com.javapid.service.PidCouponsService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.javapid.service.graphql.GraphQLService.generateParametersData;

@Component
public class NivoBarDataFetcher implements DataFetcher<List<NivoBarDataByMonth>> {

	private final PidCouponsService couponsService;

	public NivoBarDataFetcher(PidCouponsService couponsService) {
		this.couponsService = couponsService;
	}

	@Override
	public List<NivoBarDataByMonth> get(DataFetchingEnvironment dataFetchingEnvironment) {
		return couponsService.getNivoBarData(generateParametersData(dataFetchingEnvironment));
	}
}
