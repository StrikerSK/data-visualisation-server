package com.charts.nivo.controller;

import com.charts.nivo.service.graphql.GraphQLService;
import graphql.ExecutionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NivoGraphController {

    private final GraphQLService graphQLService;

    public NivoGraphController(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @PostMapping("/graphql")
    public ResponseEntity<Object> getAllBarData(@RequestBody String query){
        ExecutionResult executionResult = graphQLService.getGraphQL().execute(query);

        return new ResponseEntity<>(executionResult, HttpStatus.OK);
    }

}
