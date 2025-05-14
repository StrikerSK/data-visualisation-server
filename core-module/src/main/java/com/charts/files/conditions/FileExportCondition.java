package com.charts.files.conditions;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class FileExportCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Boolean filesEnabledProperty = FileCondition.getEnabledProperty(context);
        Boolean exportEnabledProperty = FileCondition.getEnabledProperty(context, "com.charts.files.exports.enabled");
        return new ConditionOutcome(filesEnabledProperty && exportEnabledProperty, "File export enabled");
    }

}
