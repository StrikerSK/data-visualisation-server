package com.charts.nivo.service.graphql;

import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.api.ticket.entity.TicketsParameters;
import graphql.schema.DataFetchingEnvironment;

class GraphParameters {

    static CouponsParameters fetchCouponParameters(DataFetchingEnvironment env) {
        return new CouponsParameters(
                env.getArgument("validity"),
                env.getArgument("type"),
                env.getArgument("month"),
                parseYears(env.getArgument("year")),
                env.getArgument("person")
        );
    }

    static TicketsParameters fetchTicketParameters(DataFetchingEnvironment env) {
        Boolean discounted = env.getArgument("discounted");
        return new TicketsParameters(
                env.getArgument("month"),
                parseYears(env.getArgument("year")),
                discounted == null ? null : java.util.List.of(discounted),
                env.getArgument("ticketType")
        );
    }

    private static java.util.List<Integer> parseYears(java.util.List<String> years) {
        if (years == null) {
            return null;
        }

        return years.stream()
                .map(Integer::valueOf)
                .toList();
    }

}
