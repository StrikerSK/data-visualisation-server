package com.charts.api.ticket.utils;

import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.api.ticket.entity.enums.TicketType;
import com.charts.general.utils.AbstractFilterUtils;

import java.util.List;

public class TicketFilterUtils extends AbstractFilterUtils {

    public static List<UpdateTicketEntity> filterByDiscounted(List<UpdateTicketEntity> coupons, List<Boolean> values) {
        return filterByNonEnum(coupons, values, UpdateTicketEntity::getDiscounted);
    }

    public static List<UpdateTicketEntity> filterByTypes(List<UpdateTicketEntity> coupons, List<TicketType> values) {
        return filterByValue(coupons, values, UpdateTicketEntity::getTicketType);
    }

}
