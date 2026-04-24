package com.charts.nivo.controller;

import com.charts.nivo.entity.NivoDataXY;
import com.charts.nivo.entity.NivoLineData;
import com.charts.nivo.entity.NivoPieData;
import com.charts.nivo.service.NivoGraphQLService;
import com.charts.nivo.service.graphql.MonthBarDataFetcher;
import com.charts.nivo.service.graphql.NivoBarDataFetcher;
import com.charts.nivo.service.graphql.NivoLineDataFetcher;
import com.charts.nivo.service.graphql.NivoPieDataFetcher;
import com.charts.nivo.service.graphql.PersonBarDataFetcher;
import com.charts.nivo.service.graphql.SellTypeBarDataFetcher;
import com.charts.nivo.service.graphql.TicketBarDataFetcher;
import com.charts.nivo.service.graphql.ValidityBarDataFetcher;
import graphql.GraphqlErrorException;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NivoGraphControllerTest {

    private MockMvc mockMvc;
    private StubBarDataFetcher nivoBarDataFetcher;
    private StubLineDataFetcher nivoLineDataFetcher;
    private StubPieDataFetcher nivoPieDataFetcher;

    @BeforeMethod
    public void setUp() throws Exception {
        nivoBarDataFetcher = new StubBarDataFetcher();
        nivoLineDataFetcher = new StubLineDataFetcher();
        nivoPieDataFetcher = new StubPieDataFetcher();

        NivoGraphQLService graphQLService = new NivoGraphQLService(
                nivoBarDataFetcher,
                nivoLineDataFetcher,
                nivoPieDataFetcher,
                new StubPersonBarDataFetcher(),
                new StubMonthBarDataFetcher(),
                new StubValidityBarDataFetcher(),
                new StubSellTypeBarDataFetcher(),
                new StubTicketBarDataFetcher()
        );
        ReflectionTestUtils.setField(graphQLService, "resource", new ClassPathResource("schema.graphqls"));
        graphQLService.loadSchema();

        NivoGraphController controller = new NivoGraphController(graphQLService);
        ReflectionTestUtils.setField(controller, "schemaResource", new ClassPathResource("schema.graphqls"));

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void executesBarQuery() throws Exception {
        nivoBarDataFetcher.response = List.of(Map.of("label", "2024", "adults", 10, "portable", 2));

        mockMvc.perform(post("/nivo/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "query": "query { nivoBarData(kind: \\"coupon\\", upperGroup: \\"year\\", lowerGroup: \\"person\\") { ... on PersonBarData { label adults portable } } }"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nivoBarData", hasSize(1)))
                .andExpect(jsonPath("$.data.nivoBarData[0].label").value("2024"))
                .andExpect(jsonPath("$.data.nivoBarData[0].adults").value(10))
                .andExpect(jsonPath("$.data.nivoBarData[0].portable").value(2));
    }

    @Test
    public void executesLineQueryForCouponKind() throws Exception {
        nivoLineDataFetcher.response = List.of(new NivoLineData("Adults", List.of(new NivoDataXY("January", 12L))));

        mockMvc.perform(post("/nivo/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "query": "query { nivoLineData(kind: \\"coupon\\", upperGroup: \\"year\\", lowerGroup: \\"month\\") { id data { x y } } }"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nivoLineData", hasSize(1)))
                .andExpect(jsonPath("$.data.nivoLineData[0].id").value("Adults"))
                .andExpect(jsonPath("$.data.nivoLineData[0].data[0].x").value("January"))
                .andExpect(jsonPath("$.data.nivoLineData[0].data[0].y").value(12));
    }

    @Test
    public void executesPieQueryForTicketKind() throws Exception {
        nivoPieDataFetcher.response = List.of(new NivoPieData("One Day", 4));

        mockMvc.perform(post("/nivo/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "query": "query { nivoPieData(kind: \\"ticket\\", grouping: \\"ticket\\") { id label value } }"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nivoPieData", hasSize(1)))
                .andExpect(jsonPath("$.data.nivoPieData[0].id").value("One Day"))
                .andExpect(jsonPath("$.data.nivoPieData[0].value").value(4));
    }

    @Test
    public void returnsGraphQlErrorForUnsupportedKind() throws Exception {
        nivoPieDataFetcher.exception = GraphqlErrorException.newErrorException()
                .message("Unsupported kind: unexpected")
                .build();

        mockMvc.perform(post("/nivo/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "query": "query { nivoPieData(kind: \\"unexpected\\", grouping: \\"ticket\\") { id label value } }"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", containsString("Unsupported kind: unexpected")));
    }

    @Test
    public void returnsValidationErrorForMissingRequiredArgument() throws Exception {
        mockMvc.perform(post("/nivo/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "query": "query { nivoPieData(grouping: \\"ticket\\") { id } }"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", containsString("kind")));
    }

    @Test
    public void returnsBadRequestWhenQueryBodyIsBlank() throws Exception {
        mockMvc.perform(post("/nivo/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "query": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message").value("GraphQL query must not be blank"));
    }

    @Test
    public void exposesSameSchemaFile() throws Exception {
        String schema = new ClassPathResource("schema.graphqls")
                .getContentAsString(StandardCharsets.UTF_8);

        mockMvc.perform(get("/nivo/schema"))
                .andExpect(status().isOk())
                .andExpect(content().string(schema));
    }

    private static final class StubBarDataFetcher extends NivoBarDataFetcher {
        private List<Map<String, Object>> response = List.of();

        private StubBarDataFetcher() {
            super(new StubPersonBarDataFetcher(), new StubMonthBarDataFetcher(), new StubValidityBarDataFetcher(), new StubSellTypeBarDataFetcher(), new StubTicketBarDataFetcher());
        }

        @Override
        public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
            return response;
        }
    }

    private static final class StubLineDataFetcher extends NivoLineDataFetcher {
        private List<NivoLineData> response = List.of();

        private StubLineDataFetcher() {
            super(null, null);
        }

        @Override
        public List<NivoLineData> get(DataFetchingEnvironment dataFetchingEnvironment) {
            return response;
        }
    }

    private static final class StubPieDataFetcher extends NivoPieDataFetcher {
        private List<NivoPieData> response = List.of();
        private RuntimeException exception;

        private StubPieDataFetcher() {
            super(null, null);
        }

        @Override
        public List<NivoPieData> get(DataFetchingEnvironment dataFetchingEnvironment) {
            if (exception != null) {
                throw exception;
            }
            return response;
        }
    }

    private static final class StubPersonBarDataFetcher extends PersonBarDataFetcher {
        private StubPersonBarDataFetcher() {
            super(null);
        }

        @Override
        public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
            return List.of();
        }
    }

    private static final class StubMonthBarDataFetcher extends MonthBarDataFetcher {
        private StubMonthBarDataFetcher() {
            super(null, null);
        }

        @Override
        public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
            return List.of();
        }
    }

    private static final class StubValidityBarDataFetcher extends ValidityBarDataFetcher {
        private StubValidityBarDataFetcher() {
            super(null);
        }

        @Override
        public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
            return List.of();
        }
    }

    private static final class StubSellTypeBarDataFetcher extends SellTypeBarDataFetcher {
        private StubSellTypeBarDataFetcher() {
            super(null);
        }

        @Override
        public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
            return List.of();
        }
    }

    private static final class StubTicketBarDataFetcher extends TicketBarDataFetcher {
        private StubTicketBarDataFetcher() {
            super(null);
        }

        @Override
        public List<Map<String, Object>> get(DataFetchingEnvironment dataFetchingEnvironment) {
            return List.of();
        }
    }
}
