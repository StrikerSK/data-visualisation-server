package com.charts.nivo.service.graphql;

import com.charts.api.coupon.entity.CouponsParameters;
import graphql.GraphQL;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Service
@Getter
public class GraphQLService {

	@Value("classpath:schema.graphqls")
	Resource resource;

	private GraphQL graphQL;

	private final NivoBarDataFetcher nivoBarDataFetcher;
	private final NivoLineDataFetcher nivoLineDataFetcher;
	private final NivoPieDataFetcher nivoPieDataFetcher;
	private final PersonBarDataFetcher personBarDataFetcher;

	public GraphQLService(NivoBarDataFetcher nivoBarDataFetcher, NivoLineDataFetcher nivoLineDataFetcher, NivoPieDataFetcher nivoPieDataFetcher, PersonBarDataFetcher personBarDataFetcher	) {
		this.nivoBarDataFetcher = nivoBarDataFetcher;
		this.nivoLineDataFetcher = nivoLineDataFetcher;
		this.nivoPieDataFetcher = nivoPieDataFetcher;
		this.personBarDataFetcher = personBarDataFetcher;
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
		List<String> persons = List.of("Deti", "Juniori", "Študenti", "Dospelý", "Dôchodcovia", "Prenosná");

		return RuntimeWiring.newRuntimeWiring()
				.type("Query", typeWiring -> typeWiring
						.dataFetcher("nivoBarData", nivoBarDataFetcher)
						.dataFetcher("nivoLineData", nivoLineDataFetcher)
						.dataFetcher("nivoPieData", nivoPieDataFetcher)
						.dataFetcher("PersonBarData", personBarDataFetcher)
				)
				.type("NivoBarData", builder -> builder.typeResolver(env -> {
					Map<String, Object> map = env.getObject();
					if (persons.stream().anyMatch(map::containsKey)) {
						return env.getSchema().getObjectType("PersonBarData");
					} else if (map.containsKey("january")) {
						return env.getSchema().getObjectType("MonthBarData");
					}
					return null;
				}))
				.build();
	}

	static CouponsParameters generateParametersData(DataFetchingEnvironment env) {
		return CouponsParameters.builder()
				.month(env.getArgument("month"))
				.validity(env.getArgument("validity"))
				.sellType(env.getArgument("type"))
				.year(env.getArgument("year"))
				.person(env.getArgument("person"))
				.build();
	}
}
