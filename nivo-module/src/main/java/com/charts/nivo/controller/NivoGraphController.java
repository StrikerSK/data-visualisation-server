package com.charts.nivo.controller;

import com.charts.nivo.configuration.GraphCondition;
import com.charts.nivo.service.NivoGraphQLService;
import graphql.ExecutionResult;
import graphql.kickstart.execution.GraphQLRequest;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/nivo")
@Conditional(GraphCondition.class)
public class NivoGraphController {

    @Value("classpath:schema.graphqls")
    private Resource schemaResource;

    private final NivoGraphQLService nivoGraphQLService;

    public NivoGraphController(NivoGraphQLService nivoGraphQLService) {
        this.nivoGraphQLService = nivoGraphQLService;
    }

    @PostMapping(path = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> executeGraphQL(@RequestBody GraphQLRequest request) {
        if (request == null || request.getQuery() == null || request.getQuery().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errors", java.util.List.of(Map.of("message", "GraphQL query must not be blank"))));
        }

        ExecutionResult result = nivoGraphQLService.getGraphQL()
                .execute(builder -> builder
                        .query(request.getQuery())
                        .operationName(request.getOperationName())
                        .variables(request.getVariables() != null ? request.getVariables() : Map.of())
                );

        return ResponseEntity.ok(result.toSpecification());
    }

    @GetMapping(path = "/schema", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getSchema() throws IOException {
        String sdl;
        try (InputStream inputStream = schemaResource.getInputStream()) {
            sdl = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        return ResponseEntity.ok(sdl);
    }

}
