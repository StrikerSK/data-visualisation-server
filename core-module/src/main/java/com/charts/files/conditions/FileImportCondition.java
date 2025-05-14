package com.charts.files.conditions;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class FileImportCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String property = context.getEnvironment().getProperty("com.charts.files.imports.enabled");
        Boolean isEnabled = "true".equalsIgnoreCase(property);
        return new ConditionOutcome(FileCondition.getFileEnabledProperty(context) && isEnabled, "File export enabled");
    }

}
