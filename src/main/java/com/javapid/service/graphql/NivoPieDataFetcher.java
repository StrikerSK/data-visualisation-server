package com.javapid.service.graphql;

import com.javapid.entity.nivo.pie.NivoPieAbstractData;
import com.javapid.service.PidCouponsService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.javapid.service.graphql.GraphQLService.generateParametersData;

@Component
public class NivoPieDataFetcher implements DataFetcher<List<NivoPieAbstractData>> {

	private final PidCouponsService couponsService;

	public NivoPieDataFetcher(PidCouponsService couponsService) {
		this.couponsService = couponsService;
	}

	@Override
	public List<NivoPieAbstractData> get(DataFetchingEnvironment dataFetchingEnvironment) {
		return couponsService.getNivoPieData(generateParametersData(dataFetchingEnvironment));
	}
}
