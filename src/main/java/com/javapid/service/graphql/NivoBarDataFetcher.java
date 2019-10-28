package com.javapid.service.graphql;

import com.javapid.entity.PidCouponsParameters;
import com.javapid.entity.nivo.bar.NivoBarDataByMonth;
import com.javapid.service.PidCouponsService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NivoBarDataFetcher implements DataFetcher<List<NivoBarDataByMonth>> {

	private final PidCouponsService couponsService;

	public NivoBarDataFetcher(PidCouponsService couponsService) {
		this.couponsService = couponsService;
	}

	@Override
	public List<NivoBarDataByMonth> get(DataFetchingEnvironment dataFetchingEnvironment) {
		List<String> months = dataFetchingEnvironment.getArgument("months");
		List<String> validity = dataFetchingEnvironment.getArgument("validity");
		List<String> sellType = dataFetchingEnvironment.getArgument("sellType");
		List<String> year = dataFetchingEnvironment.getArgument("year");
		List<String> persons = dataFetchingEnvironment.getArgument("person");
		return couponsService.getNivoBarData(new PidCouponsParameters(validity, sellType, months, year, persons));
	}
}
