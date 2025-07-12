package com.charts.nivo.service.graphql;

import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.api.ticket.entity.TicketsParameters;
import graphql.schema.DataFetchingEnvironment;

class GraphParameters {

    static CouponsParameters fetchCouponParameters(DataFetchingEnvironment env) {
        return CouponsParameters.builder()
                .month(env.getArgument("month"))
                .validity(env.getArgument("validity"))
                .sellType(env.getArgument("type"))
                .year(env.getArgument("year"))
                .person(env.getArgument("person"))
                .build();
    }

    static TicketsParameters fetchTicketParameters(DataFetchingEnvironment env) {
        return TicketsParameters.builder()
                .month(env.getArgument("month"))
                .year(env.getArgument("year"))
                .discounted(env.getArgument("discounted"))
                .ticketType(env.getArgument("ticketType"))
                .build();
    }

}
