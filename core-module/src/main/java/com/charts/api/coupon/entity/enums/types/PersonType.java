package com.charts.api.coupon.entity.enums.types;

import com.charts.general.entity.enums.IEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

import static com.charts.api.coupon.constants.EnumerationCouponConstants.ADULT_VALUE;
import static com.charts.api.coupon.constants.EnumerationCouponConstants.CHILDREN_VALUE;
import static com.charts.api.coupon.constants.EnumerationCouponConstants.JUNIOR_VALUE;
import static com.charts.api.coupon.constants.EnumerationCouponConstants.PORTABLE_VALUE;
import static com.charts.api.coupon.constants.EnumerationCouponConstants.SENIOR_VALUE;
import static com.charts.api.coupon.constants.EnumerationCouponConstants.STUDENT_VALUE;

@Getter
public enum PersonType implements IEnum {

    ADULT(ADULT_VALUE, "adults", 4),
    SENIOR(SENIOR_VALUE, "seniors", 5),
    JUNIOR(JUNIOR_VALUE, "juniors", 2),
    PORTABLE(PORTABLE_VALUE, "portables", 6),
    STUDENT(STUDENT_VALUE, "students", 3),
    CHILDREN(CHILDREN_VALUE, "children", 1);

    private final String value;
    private final String systemValue;
    private final Integer orderValue;

    PersonType(String value, String systemValue, Integer orderValue) {
        this.value = value;
        this.systemValue = systemValue;
        this.orderValue = orderValue;
    }

    public static Optional<PersonType> getPersonType(String label) {
        return Arrays.stream(PersonType.values())
                .filter(c -> c.getValue().equals(label))
                .findFirst();
    }

    public static String convertPersonType(String label) {
        return Arrays.stream(PersonType.values())
                .filter(c -> c.getValue().equals(label))
                .findFirst().get().getSystemValue();
    }

}
