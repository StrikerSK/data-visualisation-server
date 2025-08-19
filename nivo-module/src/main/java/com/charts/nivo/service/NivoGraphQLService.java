package com.charts.nivo.service;

import com.charts.nivo.configuration.GraphCondition;
import com.charts.nivo.service.graphql.*;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
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

	private final TicketBarDataFetcher ticketBarDataFetcher;
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
			SellTypeBarDataFetcher sellTypeBarDataFetcher, TicketBarDataFetcher ticketBarDataFetcher
	) {
		this.nivoBarDataFetcher = nivoBarDataFetcher;
		this.nivoLineDataFetcher = nivoLineDataFetcher;
		this.nivoPieDataFetcher = nivoPieDataFetcher;
		this.personBarDataFetcher = personBarDataFetcher;
		this.monthBarDataFetcher = monthBarDataFetcher;
		this.validityBarDataFetcher = validityBarDataFetcher;
		this.sellTypeBarDataFetcher = sellTypeBarDataFetcher;
		this.ticketBarDataFetcher = ticketBarDataFetcher;
	}

	@PostConstruct
	public void loadSchema() throws IOException {
		try (InputStream loadedSchema = resource.getInputStream()) {
			TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(new InputStreamReader(loadedSchema));
			RuntimeWiring wiring = buildRuntimeWiring();
			GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
			graphQL = GraphQL.newGraphQL(schema).build();
		}
	}

	private RuntimeWiring buildRuntimeWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.type("Query", this::enhanceQueryBuilder)
				.type("NivoBarData", this::enhanceBarDataBuilder)
				.build();
	}

    private TypeRuntimeWiring.Builder enhanceQueryBuilder(TypeRuntimeWiring.Builder builder) {
        return builder
                .dataFetcher("nivoBarData", nivoBarDataFetcher)
                .dataFetcher("nivoLineData", nivoLineDataFetcher)
                .dataFetcher("nivoPieData", nivoPieDataFetcher)
                .dataFetcher("MonthBarData", monthBarDataFetcher)
                .dataFetcher("PersonBarData", personBarDataFetcher)
                .dataFetcher("ValidityBarData", validityBarDataFetcher)
                .dataFetcher("SellTypeBarData", sellTypeBarDataFetcher)
                .dataFetcher("TicketBarData", ticketBarDataFetcher);
    }

    private TypeRuntimeWiring.Builder enhanceBarDataBuilder(TypeRuntimeWiring.Builder builder) {
        return builder.typeResolver(env -> {
            String lowerGroup = env.getArguments().get("lowerGroup").toString().toLowerCase();
            return switch (lowerGroup) {
                case "person" -> env.getSchema().getObjectType("PersonBarData");
                case "month" -> env.getSchema().getObjectType("MonthBarData");
                case "validity" -> env.getSchema().getObjectType("ValidityBarData");
                case "type" -> env.getSchema().getObjectType("SellTypeBarData");
                case "ticket"  -> env.getSchema().getObjectType("TicketBarData");
                default -> null;
            };
        });
    }

}
