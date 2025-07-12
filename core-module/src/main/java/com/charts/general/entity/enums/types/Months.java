package com.charts.general.entity.enums.types;

import com.charts.general.entity.enums.IEnum;
import lombok.Getter;

import static com.charts.general.entity.constants.EnumerationConstants.*;

@Getter
public enum Months implements IEnum {

    JANUARY(JANUARY_VALUE, "january", 1),
    FEBRUARY(FEBRUARY_VALUE, "february", 2),
    MARCH(MARCH_VALUE, "march", 3),
    APRIL(APRIL_VALUE, "april", 4),
    MAY(MAY_VALUE, "may", 5),
    JUNE(JUNE_VALUE, "june", 6),
    JULY(JULY_VALUE, "july", 7),
    AUGUST(AUGUST_VALUE, "august", 8),
    SEPTEMBER(SEPTEMBER_VALUE, "september", 9),
    OCTOBER(OCTOBER_VALUE, "october", 10),
    NOVEMBER(NOVEMBER_VALUE, "november", 11),
    DECEMBER(DECEMBER_VALUE, "december", 12);

    private final String value;
    private final String systemValue;
    private final Integer orderValue;

    Months(String value, String systemValue, Integer orderValue) {
        this.value = value;
        this.systemValue = systemValue;
        this.orderValue = orderValue;
    }

}
