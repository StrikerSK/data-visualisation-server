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
	private final MonthBarDataFetcher monthBarDataFetcher;

	public GraphQLService(NivoBarDataFetcher nivoBarDataFetcher, NivoLineDataFetcher nivoLineDataFetcher, NivoPieDataFetcher nivoPieDataFetcher, PersonBarDataFetcher personBarDataFetcher, MonthBarDataFetcher monthBarDataFetcher) {
		this.nivoBarDataFetcher = nivoBarDataFetcher;
		this.nivoLineDataFetcher = nivoLineDataFetcher;
		this.nivoPieDataFetcher = nivoPieDataFetcher;
		this.personBarDataFetcher = personBarDataFetcher;
		this.monthBarDataFetcher = monthBarDataFetcher;
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
				.type("Query", typeWiring -> typeWiring
						.dataFetcher("nivoBarData", nivoBarDataFetcher)
						.dataFetcher("nivoLineData", nivoLineDataFetcher)
						.dataFetcher("nivoPieData", nivoPieDataFetcher)
						.dataFetcher("PersonBarData", personBarDataFetcher)
						.dataFetcher("MonthBarData", monthBarDataFetcher)
				)
				.type("NivoBarData", builder -> builder.typeResolver(env -> {
					String barType = env.getArguments().get("barType").toString().toLowerCase();
					if (barType.equals("person")) {
						return env.getSchema().getObjectType("PersonBarData");
					} else if (barType.equals("month")) {
						return env.getSchema().getObjectType("MonthBarData");
					} else {
                        return null;
                    }
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
