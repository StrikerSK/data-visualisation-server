package com.javapid.service.graphql;

import com.javapid.entity.nivo.line.NivoLineAbstractData;
import com.javapid.service.PidCouponsService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NivoLineDataFetcher implements DataFetcher<List<NivoLineAbstractData>> {

	private final PidCouponsService couponsService;

	public NivoLineDataFetcher(PidCouponsService couponsService) {
		this.couponsService = couponsService;
	}

	@Override
	public List<NivoLineAbstractData> get(DataFetchingEnvironment dataFetchingEnvironment) {
		List<String> months = dataFetchingEnvironment.getArgument("months");
		List<String> validity = dataFetchingEnvironment.getArgument("validity");
		List<String> sellType = dataFetchingEnvironment.getArgument("sellType");
		List<String> year = dataFetchingEnvironment.getArgument("year");
		List<String> person = dataFetchingEnvironment.getArgument("person");
		return couponsService.getNivoLineData(validity,sellType,months,year,person);
	}
}
