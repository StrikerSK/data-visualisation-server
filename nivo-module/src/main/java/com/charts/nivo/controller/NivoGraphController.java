package com.charts.nivo.controller;

import com.charts.nivo.service.NivoGraphQLService;
import graphql.ExecutionResult;
import graphql.kickstart.execution.GraphQLRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/nivo")
public class NivoGraphController {

    @Value("classpath:graphql/schema.graphqls")
    private Resource schemaResource;

    private final NivoGraphQLService nivoGraphQLService;

    public NivoGraphController(NivoGraphQLService nivoGraphQLService) {
        this.nivoGraphQLService = nivoGraphQLService;
    }

    @PostMapping(path = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> executeGraphQL(@RequestBody GraphQLRequest request) {
        ExecutionResult result = nivoGraphQLService.getGraphQL()
                .execute(builder -> builder
                        .query(request.getQuery())
                        .variables(request.getVariables() != null ? request.getVariables() : Map.of())
                );

        return ResponseEntity.ok(result.toSpecification());
    }

    @GetMapping(path = "/schema", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getSchema() throws IOException {
        String sdl = new String(schemaResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok(sdl);
    }

}
