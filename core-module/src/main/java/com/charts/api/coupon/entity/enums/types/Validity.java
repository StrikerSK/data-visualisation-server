package com.charts.api.coupon.entity.enums.types;

import com.charts.general.entity.enums.IEnum;
import lombok.Getter;

import static com.charts.api.coupon.constants.EnumerationCouponConstants.*;

@Getter
public enum Validity implements IEnum {

    MONTHLY(ONE_MONTH, 1),
    THREE_MONTHS(THREE_MONTH, 2),
    FIVE_MONTHS(FIVE_MONTH, 3),
    YEARLY(ONE_YEAR, 4);

    @Getter
    private final String value;
    private final Integer orderValue;

    Validity(String value, Integer orderValue) {
        this.value = value;
        this.orderValue = orderValue;
    }

}
