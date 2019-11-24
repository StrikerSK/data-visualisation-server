package com.javapid.service.graphql;

import com.javapid.entity.PidCouponsParameters;
import graphql.GraphQL;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class GraphQLService {

	@Value("classpath:schema.graphql")
	Resource resource;

	private GraphQL graphQL;

	private final NivoBarDataFetcher nivoBarDataFetcher;

	private final NivoLineDataFetcher nivoLineDataFetcher;

	private final NivoPieDataFetcher nivoPieDataFetcher;

	public GraphQLService(NivoBarDataFetcher nivoBarDataFetcher, NivoLineDataFetcher nivoLineDataFetcher, NivoPieDataFetcher nivoPieDataFetcher) {
		this.nivoBarDataFetcher = nivoBarDataFetcher;
		this.nivoLineDataFetcher = nivoLineDataFetcher;
		this.nivoPieDataFetcher = nivoPieDataFetcher;
	}

	@PostConstruct
	public void loadSchema() throws IOException {
		File schemaFile = resource.getFile();
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
		RuntimeWiring wiring = buildRuntimeWiring();
		GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
		graphQL = GraphQL.newGraphQL(schema).build();
	}

	private RuntimeWiring buildRuntimeWiring() {

		return RuntimeWiring.newRuntimeWiring()
				.type("Query", typeWiring -> typeWiring
						.dataFetcher("nivoBarData", nivoBarDataFetcher)
						.dataFetcher("nivoLineData", nivoLineDataFetcher)
						.dataFetcher("nivoPieData", nivoPieDataFetcher))
				.build();
	}

	public GraphQL getGraphQL() {
		return graphQL;
	}

	static PidCouponsParameters generateParametersData(DataFetchingEnvironment dataFetchingEnvironment) {
		PidCouponsParameters parameters = new PidCouponsParameters();
		parameters.setMonth(dataFetchingEnvironment.getArgument("months"));
		parameters.setValidity(dataFetchingEnvironment.getArgument("validity"));
		parameters.setSellType(dataFetchingEnvironment.getArgument("sellType"));
		parameters.setYear(dataFetchingEnvironment.getArgument("year"));
		parameters.setPerson(dataFetchingEnvironment.getArgument("person"));
		return parameters;
	}
}
