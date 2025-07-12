package com.charts.nivo.service.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class NivoBarDataFetcher implements DataFetcher<List<Map<String, Object>>> {

	private final PersonBarDataFetcher personBarDataFetcher;
	private final MonthBarDataFetcher monthBarDataFetcher;

	public NivoBarDataFetcher(PersonBarDataFetcher personBarDataFetcher, MonthBarDataFetcher monthBarDataFetcher) {
		this.personBarDataFetcher = personBarDataFetcher;
		this.monthBarDataFetcher = monthBarDataFetcher;
	}

	@Override
	public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
		String lowerGroup = dataFetchingEnvironment.getArgument("lowerGroup");
        return switch (lowerGroup) {
            case "person" -> personBarDataFetcher.get(dataFetchingEnvironment);  // return NivoBarData instance
            case "month" -> monthBarDataFetcher.get(dataFetchingEnvironment); // return NivoLineData instance
            default -> null;
        };
	}

}
