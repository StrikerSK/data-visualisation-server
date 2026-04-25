package com.charts.api.ticket.utils;

import com.charts.api.ticket.entity.enums.TicketType;
import com.charts.general.entity.enums.types.EnumAdapter;
import com.charts.general.entity.enums.types.Months;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.general.utils.AbstractGroupingUtils;

import java.util.List;
import java.util.Map;

public final class TicketGroupingUtils {

    private TicketGroupingUtils() {
    }

    public static Map<TicketType, List<UpdateTicketEntity>> groupByTicketType(List<UpdateTicketEntity> entries) {
        return AbstractGroupingUtils.groupValues(entries, UpdateTicketEntity::getTicketType);
    }

    public static Map<EnumAdapter, List<UpdateTicketEntity>> groupByDiscounted(List<UpdateTicketEntity> entries) {
        return AbstractGroupingUtils.groupValues(entries, e -> new EnumAdapter(e.getDiscounted()));
    }

    public static Map<EnumAdapter, List<UpdateTicketEntity>> groupByYear(List<UpdateTicketEntity> entries) {
        return AbstractGroupingUtils.groupByYear(entries);
    }

    public static Map<Months, List<UpdateTicketEntity>> groupByMonth(List<UpdateTicketEntity> entries) {
        return AbstractGroupingUtils.groupByMonth(entries);
    }

    public static Integer aggregateGroupSum(List<UpdateTicketEntity> entries) {
        return AbstractGroupingUtils.aggregateGroupSum(entries);
    }

}
