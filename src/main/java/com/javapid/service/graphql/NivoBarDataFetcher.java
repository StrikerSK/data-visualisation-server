package com.javapid.service.graphql;

import com.javapid.entity.nivo.NivoBarData;
import com.javapid.service.PidCouponsService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NivoBarDataFetcher implements DataFetcher<List<NivoBarData>> {

	private final PidCouponsService couponsService;

	public NivoBarDataFetcher(PidCouponsService couponsService) {
		this.couponsService = couponsService;
	}

	@Override
	public List<NivoBarData> get(DataFetchingEnvironment dataFetchingEnvironment) {
		List<String> months = dataFetchingEnvironment.getArgument("months");
		List<String> validity = dataFetchingEnvironment.getArgument("validity");
		List<String> sellType = dataFetchingEnvironment.getArgument("sellType");
		List<String> year = dataFetchingEnvironment.getArgument("year");
		return couponsService.getNivoBarData(validity,sellType,months,year);
	}
}
