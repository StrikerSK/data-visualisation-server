package com.charts.api.ticket.utils;

import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.exception.InvalidParameterException;
import com.charts.general.utils.AbstractFunctionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class TicketFunctionUtils {

    public static final String DISCOUNTED_GROUP = "discounted";
    public static final String TICKET_GROUP = "ticket";
    public static final String MONTH_GROUP = AbstractFunctionUtils.MONTH_GROUP;
    public static final String YEAR_GROUP = AbstractFunctionUtils.YEAR_GROUP;

    private TicketFunctionUtils() {
    }

    public static void validateGroups(String upperGroup, String lowerGroup) {
        AbstractFunctionUtils.validateGroups(upperGroup, lowerGroup);
    }

    public static Function<List<UpdateTicketEntity>, Map<IEnum, List<UpdateTicketEntity>>> createGrouping(String groupName) {
        Function<List<UpdateTicketEntity>, Map<IEnum, List<UpdateTicketEntity>>> convertedData;

        switch (groupName.toLowerCase()) {
            case TICKET_GROUP:
                convertedData = groupBy(TicketGroupingUtils::groupByTicketType);
                break;
            case MONTH_GROUP:
                convertedData = groupBy(TicketGroupingUtils::groupByMonth);
                break;
            case DISCOUNTED_GROUP:
                convertedData = groupBy(TicketGroupingUtils::groupByDiscounted);
                break;
            case YEAR_GROUP:
                convertedData = groupBy(TicketGroupingUtils::groupByYear);
                break;
            default:
                throw new InvalidParameterException(
                        String.format("Unknown group name: %s! Available groups: %s, %s, %s, %s", groupName, YEAR_GROUP, MONTH_GROUP, TICKET_GROUP, DISCOUNTED_GROUP)
                );
        }

        return convertedData;
    }

    private static <T extends IEnum> Function<List<UpdateTicketEntity>, Map<IEnum, List<UpdateTicketEntity>>> groupBy(
            Function<List<UpdateTicketEntity>, Map<T, List<UpdateTicketEntity>>> groupingFunction
    ) {
        return entries -> new LinkedHashMap<>(groupingFunction.apply(entries));
    }

}
