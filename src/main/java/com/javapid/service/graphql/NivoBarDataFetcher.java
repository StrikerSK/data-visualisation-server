package com.javapid.service.graphql;

import com.javapid.entity.nivo.NivoBarData;
import com.javapid.service.NivoDataService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NivoBarDataFetcher implements DataFetcher<List<NivoBarData>> {

	private final NivoDataService nivoDataService;

	public NivoBarDataFetcher(NivoDataService nivoDataService) {
		this.nivoDataService = nivoDataService;
	}

	@Override
	public List<NivoBarData> get(DataFetchingEnvironment dataFetchingEnvironment) {
		List<String> months = dataFetchingEnvironment.getArgument("months");
		List<String> validity = dataFetchingEnvironment.getArgument("validity");
		List<String> sellType = dataFetchingEnvironment.getArgument("sellType");
		return nivoDataService.getNivoBarData(validity,sellType,months);
	}
}
