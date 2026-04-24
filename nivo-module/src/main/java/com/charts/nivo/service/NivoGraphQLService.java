package com.charts.nivo.service;

import com.charts.nivo.configuration.GraphCondition;
import com.charts.nivo.service.graphql.*;
import graphql.GraphQL;
import graphql.TypeResolutionEnvironment;
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
import java.util.Map;
import java.util.Set;

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
                .dataFetcher("nivoPieData", nivoPieDataFetcher);
    }

    private TypeRuntimeWiring.Builder enhanceBarDataBuilder(TypeRuntimeWiring.Builder builder) {
        return builder.typeResolver(this::resolveBarDataType);
    }

    private graphql.schema.GraphQLObjectType resolveBarDataType(TypeResolutionEnvironment env) {
        Object value = env.getObject();
        if (!(value instanceof Map<?, ?> mapValue)) {
            return null;
        }

        Set<String> fieldNames = mapValue.keySet().stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .collect(java.util.stream.Collectors.toSet());

        if (containsAny(fieldNames, "adults", "portable", "portables", "children", "students")) {
            return env.getSchema().getObjectType("PersonBarData");
        }
        if (containsAny(fieldNames, "january", "february", "december")) {
            return env.getSchema().getObjectType("MonthBarData");
        }
        if (containsAny(fieldNames, "one_month", "three_months", "one_year")) {
            return env.getSchema().getObjectType("ValidityBarData");
        }
        if (containsAny(fieldNames, "chip_card", "paper_coupon", "e_shop")) {
            return env.getSchema().getObjectType("SellTypeBarData");
        }
        if (containsAny(fieldNames, "fifteen_minutes", "one_day", "eleven_zones")) {
            return env.getSchema().getObjectType("TicketBarData");
        }

        return null;
    }

    private boolean containsAny(Set<String> fieldNames, String... candidates) {
        for (String candidate : candidates) {
            if (fieldNames.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

}
