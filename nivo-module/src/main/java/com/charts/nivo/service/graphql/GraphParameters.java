package com.charts.nivo.service.graphql;

import com.charts.api.coupon.entity.CouponsParameters;
import graphql.schema.DataFetchingEnvironment;

class GraphParameters {

    static CouponsParameters generateParametersData(DataFetchingEnvironment env) {
        return CouponsParameters.builder()
                .month(env.getArgument("month"))
                .validity(env.getArgument("validity"))
                .sellType(env.getArgument("type"))
                .year(env.getArgument("year"))
                .person(env.getArgument("person"))
                .build();
    }

}
