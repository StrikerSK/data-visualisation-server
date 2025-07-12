package com.charts.nivo.service;

import com.charts.nivo.configuration.GraphCondition;
import com.charts.nivo.service.graphql.MonthBarDataFetcher;
import com.charts.nivo.service.graphql.NivoBarDataFetcher;
import com.charts.nivo.service.graphql.NivoLineDataFetcher;
import com.charts.nivo.service.graphql.NivoPieDataFetcher;
import com.charts.nivo.service.graphql.PersonBarDataFetcher;
import com.charts.nivo.service.graphql.SellTypeBarDataFetcher;
import com.charts.nivo.service.graphql.ValidityBarDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
@Getter
@Conditional(GraphCondition.class)
public class NivoGraphQLService {

	@Value("classpath:schema.graphqls")
	Resource resource;

	private GraphQL graphQL;

	private final NivoBarDataFetcher nivoBarDataFetcher;
	private final NivoLineDataFetcher nivoLineDataFetcher;
	private final NivoPieDataFetcher nivoPieDataFetcher;
	private final PersonBarDataFetcher personBarDataFetcher;
	private final MonthBarDataFetcher monthBarDataFetcher;
	private final ValidityBarDataFetcher validityBarDataFetcher;
	private final SellTypeBarDataFetcher sellTypeBarDataFetcher;

	public NivoGraphQLService(
			NivoBarDataFetcher nivoBarDataFetcher, NivoLineDataFetcher nivoLineDataFetcher,
			NivoPieDataFetcher nivoPieDataFetcher, PersonBarDataFetcher personBarDataFetcher,
			MonthBarDataFetcher monthBarDataFetcher, ValidityBarDataFetcher validityBarDataFetcher,
			SellTypeBarDataFetcher sellTypeBarDataFetcher
	) {
		this.nivoBarDataFetcher = nivoBarDataFetcher;
		this.nivoLineDataFetcher = nivoLineDataFetcher;
		this.nivoPieDataFetcher = nivoPieDataFetcher;
		this.personBarDataFetcher = personBarDataFetcher;
		this.monthBarDataFetcher = monthBarDataFetcher;
		this.validityBarDataFetcher = validityBarDataFetcher;
		this.sellTypeBarDataFetcher = sellTypeBarDataFetcher;
	}

	@PostConstruct
	public void loadSchema() throws IOException {
		try (InputStream inputStream = resource.getInputStream()) {
			TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(new InputStreamReader(inputStream));
			RuntimeWiring wiring = buildRuntimeWiring();
			GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
			graphQL = GraphQL.newGraphQL(schema).build();
		}
	}

	private RuntimeWiring buildRuntimeWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.type("Query", builder -> builder
						.dataFetcher("nivoBarData", nivoBarDataFetcher)
						.dataFetcher("nivoLineData", nivoLineDataFetcher)
						.dataFetcher("nivoPieData", nivoPieDataFetcher)
						.dataFetcher("MonthBarData", monthBarDataFetcher)
						.dataFetcher("PersonBarData", personBarDataFetcher)
						.dataFetcher("ValidityBarData", validityBarDataFetcher)
						.dataFetcher("SellTypeBarData", sellTypeBarDataFetcher)
				)
				.type("NivoBarData", builder -> builder.typeResolver(env -> {
					String lowerGroup = env.getArguments().get("lowerGroup").toString().toLowerCase();
                    return switch (lowerGroup) {
                        case "person" -> env.getSchema().getObjectType("PersonBarData");
                        case "month" -> env.getSchema().getObjectType("MonthBarData");
                        case "validity" -> env.getSchema().getObjectType("ValidityBarData");
                        case "type" -> env.getSchema().getObjectType("SellTypeBarData");
                        default -> null;
                    };
				}))
				.build();
	}

}
