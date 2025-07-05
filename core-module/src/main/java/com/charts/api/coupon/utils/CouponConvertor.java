package com.charts.api.coupon.utils;

import com.charts.api.coupon.entity.v1.CouponEntityV1;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.entity.enums.types.PersonType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.charts.api.coupon.constants.EnumerationCouponConstants.*;

public class CouponConvertor {

    public static List<UpdateCouponEntity> convertCouponEntity(CouponEntityV1 couponEntityV1) {
        return fillData(couponEntityV1);
    }

    public static List<UpdateCouponEntity> convertCouponEntity(List<CouponEntityV1> couponEntities) {
        return couponEntities.stream()
                .map(CouponConvertor::fillData)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<UpdateCouponEntity> fillData(CouponEntityV1 couponEntityV1) {
        List<Pair<String, Function<CouponEntityV1, Integer>>> rawData = Stream.of(
                Pair.of(PORTABLE_VALUE, (Function<CouponEntityV1, Integer>) CouponEntityV1::getPortable),
                Pair.of(SENIOR_VALUE, (Function<CouponEntityV1, Integer>) CouponEntityV1::getSeniors),
                Pair.of(ADULT_VALUE, (Function<CouponEntityV1, Integer>) CouponEntityV1::getAdults),
                Pair.of(STUDENT_VALUE, (Function<CouponEntityV1, Integer>) CouponEntityV1::getStudents),
                Pair.of(JUNIOR_VALUE, (Function<CouponEntityV1, Integer>) CouponEntityV1::getJunior),
                Pair.of(CHILDREN_VALUE, (Function<CouponEntityV1, Integer>) CouponEntityV1::getChildren)
        ).collect(Collectors.toList());

        return rawData
                .stream()
                .map(p -> extractData(p.getLeft(), couponEntityV1, p.getRight()))
                .collect(Collectors.toList());
    }

    private static UpdateCouponEntity extractData(String personType, CouponEntityV1 couponEntityV1, Function<CouponEntityV1, Integer> function) {
        return UpdateCouponEntity.builder()
                .value(function.apply(couponEntityV1))
                .validity(couponEntityV1.getValidity())
                .sellType(couponEntityV1.getType())
                .personType(PersonType.getPersonType(personType).get())
                .month(couponEntityV1.getMonth())
                .year(couponEntityV1.getYear())
                .build();
    }

}
