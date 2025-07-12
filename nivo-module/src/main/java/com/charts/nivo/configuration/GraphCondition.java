package com.charts.nivo.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class GraphCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String isEnabled = context.getEnvironment().getProperty("com.charts.graphql.enabled");
        return new ConditionOutcome("true".equalsIgnoreCase(isEnabled), "GraphQL is enabled!");
    }

}
