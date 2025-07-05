package com.charts.api.coupon.utils;

import com.charts.general.entity.coupon.AbstractCouponTest;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.entity.enums.types.PersonType;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class CouponConvertorTest extends AbstractCouponTest {

    @Test
    public void TestCouponConverter() {
        List<UpdateCouponEntity> convertedValues = CouponConvertor.convertCouponEntity(couponEntityV11);

        Assert.assertEquals(convertedValues.size(), 6);
        Assert.assertTrue(convertedValues.stream().allMatch(e -> Objects.equals(e.getYear(), couponEntityV11.getYear())));
        Assert.assertTrue(convertedValues.stream().allMatch(e -> Objects.equals(e.getMonth(), couponEntityV11.getMonth())));

        Stream.of(
                Pair.of(PersonType.PORTABLE, couponEntityV11.getPortable()),
                Pair.of(PersonType.SENIOR, couponEntityV11.getSeniors()),
                Pair.of(PersonType.ADULT, couponEntityV11.getAdults()),
                Pair.of(PersonType.STUDENT, couponEntityV11.getStudents()),
                Pair.of(PersonType.JUNIOR, couponEntityV11.getJunior()),
                Pair.of(PersonType.CHILDREN, couponEntityV11.getChildren())
        ).forEach(p -> findCouponEntity(convertedValues, p.getLeft(), p.getRight()));
    }

    private void findCouponEntity(List<UpdateCouponEntity> entities, PersonType personType, Integer value) {
        Optional<UpdateCouponEntity> entity = entities.stream().filter(e -> e.getPersonType() == personType).findFirst();
        Assert.assertTrue(entity.isPresent(), String.format("Entity not found for %s", personType));
        Assert.assertEquals(entity.get().getValue(), value, String.format("Test failed for %s", personType));
    }

}
