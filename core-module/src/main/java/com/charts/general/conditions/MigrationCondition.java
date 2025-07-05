package com.charts.general.conditions;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MigrationCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        boolean isEnabled = "true".equalsIgnoreCase(context.getEnvironment().getProperty("com.charts.migration.enabled"));
        return new ConditionOutcome(isEnabled, "Data migration");
    }

}
