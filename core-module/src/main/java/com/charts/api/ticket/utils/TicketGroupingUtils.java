package com.charts.api.ticket.utils;

import com.charts.api.ticket.entity.enums.TicketType;
import com.charts.general.entity.enums.types.EnumAdapter;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.general.utils.AbstractGroupingUtils;

import java.util.List;
import java.util.Map;

public class TicketGroupingUtils extends AbstractGroupingUtils {

    public static Map<TicketType, List<UpdateTicketEntity>> groupByTicketType(List<UpdateTicketEntity> couponEntityList) {
        return groupValues(couponEntityList, UpdateTicketEntity::getTicketType);
    }

    public static Map<EnumAdapter, List<UpdateTicketEntity>> groupByDiscounted(List<UpdateTicketEntity> couponEntityList) {
        return groupValues(couponEntityList, e -> new EnumAdapter(e.getDiscounted()));
    }

}
