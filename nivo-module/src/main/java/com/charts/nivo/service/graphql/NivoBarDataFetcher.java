package com.charts.nivo.service.graphql;

import com.charts.nivo.configuration.GraphCondition;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Conditional(GraphCondition.class)
public class NivoBarDataFetcher implements DataFetcher<List<Map<String, Object>>> {

	private final PersonBarDataFetcher personBarDataFetcher;
	private final MonthBarDataFetcher monthBarDataFetcher;
	private final ValidityBarDataFetcher validityBarDataFetcher;
	private final SellTypeBarDataFetcher sellTypeBarDataFetcher;

	public NivoBarDataFetcher(
			PersonBarDataFetcher personBarDataFetcher, MonthBarDataFetcher monthBarDataFetcher,
			ValidityBarDataFetcher validityBarDataFetcher, SellTypeBarDataFetcher sellTypeBarDataFetcher
	) {
		this.personBarDataFetcher = personBarDataFetcher;
		this.monthBarDataFetcher = monthBarDataFetcher;
		this.validityBarDataFetcher = validityBarDataFetcher;
		this.sellTypeBarDataFetcher = sellTypeBarDataFetcher;
	}

	@Override
	public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
		String lowerGroup = dataFetchingEnvironment.getArgument("lowerGroup");
        return switch (lowerGroup) {
            case "person" -> personBarDataFetcher.get(dataFetchingEnvironment);
            case "month" -> monthBarDataFetcher.get(dataFetchingEnvironment);
            case "validity" -> validityBarDataFetcher.get(dataFetchingEnvironment);
            case "type" -> sellTypeBarDataFetcher.get(dataFetchingEnvironment);
            default -> null;
        };
	}

}
