package com.charts.files.conditions;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class FileCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return new ConditionOutcome(getFileEnabledProperty(context), "File export enabled");
    }

    protected static Boolean getFileEnabledProperty(ConditionContext context) {
        String isEnabled = context.getEnvironment().getProperty("com.charts.files.enabled");
        return "true".equalsIgnoreCase(isEnabled);
    }

}
