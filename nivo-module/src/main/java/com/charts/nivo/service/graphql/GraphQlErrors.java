package com.charts.nivo.service.graphql;

import graphql.GraphqlErrorException;

final class GraphQlErrors {

    private GraphQlErrors() {
    }

    static GraphqlErrorException invalidArgument(String message) {
        return GraphqlErrorException.newErrorException()
                .message(message)
                .build();
    }
}
