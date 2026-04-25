package com.charts.api.ticket.utils;

import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.api.ticket.entity.enums.TicketType;
import com.charts.general.entity.enums.types.Months;
import com.charts.general.utils.AbstractFilterUtils;

import java.util.List;

public final class TicketFilterUtils {

    private TicketFilterUtils() {
    }

    public static List<UpdateTicketEntity> filterByMonth(List<UpdateTicketEntity> entries, List<Months> values) {
        return AbstractFilterUtils.filterByMonth(entries, values);
    }

    public static List<UpdateTicketEntity> filterByYear(List<UpdateTicketEntity> entries, List<Integer> values) {
        return AbstractFilterUtils.filterByYear(entries, values);
    }

    public static List<UpdateTicketEntity> filterByDiscounted(List<UpdateTicketEntity> entries, List<Boolean> values) {
        return AbstractFilterUtils.filterByNonEnum(entries, values, UpdateTicketEntity::getDiscounted);
    }

    public static List<UpdateTicketEntity> filterByTypes(List<UpdateTicketEntity> entries, List<TicketType> values) {
        return AbstractFilterUtils.filterByValue(entries, values, UpdateTicketEntity::getTicketType);
    }

}
