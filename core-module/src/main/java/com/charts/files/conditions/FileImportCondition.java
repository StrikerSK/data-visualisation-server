package com.charts.files.conditions;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class FileImportCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Boolean filesEnabledProperty = FileCondition.getEnabledProperty(context);
        Boolean importEnabledProperty = FileCondition.getEnabledProperty(context, "com.charts.files.imports.enabled");
        return new ConditionOutcome(filesEnabledProperty && importEnabledProperty, "File import enabled");
    }

}
