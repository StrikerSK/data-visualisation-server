package com.charts.files.conditions;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class FileCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return new ConditionOutcome(getEnabledProperty(context), "Files are enabled!");
    }

    protected static Boolean getEnabledProperty(ConditionContext context) {
        return getEnabledProperty(context, "com.charts.files.enabled");
    }

    protected static Boolean getEnabledProperty(ConditionContext context, String propertyName) {
        String isEnabled = context.getEnvironment().getProperty(propertyName);
        return "true".equalsIgnoreCase(isEnabled);
    }

}
