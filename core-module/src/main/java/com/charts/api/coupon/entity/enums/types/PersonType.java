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

    ADULT(ADULT_VALUE, 4),
    SENIOR(SENIOR_VALUE, 5),
    JUNIOR(JUNIOR_VALUE, 2),
    PORTABLE(PORTABLE_VALUE, 6),
    STUDENT(STUDENT_VALUE, 3),
    CHILDREN(CHILDREN_VALUE, 1);

    private final String value;
    private final Integer orderValue;

    PersonType(String value, Integer orderValue) {
        this.value = value;
        this.orderValue = orderValue;
    }

    public static Optional<PersonType> getPersonType(String label) {
        return Arrays.stream(PersonType.values())
                .filter(c -> c.getValue().equals(label))
                .findFirst();
    }

}
