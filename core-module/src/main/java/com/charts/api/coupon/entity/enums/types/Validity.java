package com.charts.api.coupon.entity.enums.types;

import com.charts.general.entity.enums.IEnum;
import lombok.Data;
import lombok.Getter;

import static com.charts.api.coupon.constants.EnumerationCouponConstants.*;

@Getter
public enum Validity implements IEnum {

    MONTHLY(ONE_MONTH, "one_month", 1),
    THREE_MONTHS(THREE_MONTH, "three_months", 2),
    FIVE_MONTHS(FIVE_MONTH, "five_months", 3),
    YEARLY(ONE_YEAR, "one_year", 4);

    private final String value;
    private final String systemValue;
    private final Integer orderValue;

    Validity(String value, String systemValue, Integer orderValue) {
        this.value = value;
        this.systemValue = systemValue;
        this.orderValue = orderValue;
    }

}
