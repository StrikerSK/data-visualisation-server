package com.charts.api.coupon.utils;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.general.entity.coupon.AbstractCouponTest;
import com.charts.general.entity.enums.types.Months;
import com.charts.api.coupon.entity.enums.types.PersonType;
import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.api.coupon.entity.enums.types.Validity;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CouponFilteringUtilTest extends AbstractCouponTest {

    @Test
    public void TestMonthFiltering() {
        List<Months> searchList = Stream.of(Months.MARCH, Months.DECEMBER).collect(Collectors.toList());
        List<UpdateCouponEntity> result = CouponFilterUtils.filterByMonth(couponV2List, searchList);
        Assert.assertEquals(result.size(), 12);
        result.forEach(e -> Assert.assertTrue(searchList.contains(e.getMonth())));
    }

    @Test
    public void TestMonthFilteringNoResults() {
        List<Months> searchList = Stream.of(Months.JANUARY, Months.SEPTEMBER).collect(Collectors.toList());
        List<UpdateCouponEntity> result = CouponFilterUtils.filterByMonth(couponV2List, searchList);
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void TestPersonFiltering() {
        List<PersonType> searchList = Stream.of(PersonType.STUDENT, PersonType.ADULT, PersonType.JUNIOR).collect(Collectors.toList());
        List<UpdateCouponEntity> result = CouponFilterUtils.filterByPersonType(couponV2List, searchList);
        Assert.assertEquals(result.size(), 9);
        result.forEach(e -> Assert.assertTrue(searchList.contains(e.getPersonType())));
    }

    @Test
    public void TestYearFiltering() {
        List<Integer> searchList = Stream.of(2015, 2020).collect(Collectors.toList());
        List<UpdateCouponEntity> result = CouponFilterUtils.filterByYear(couponV2List, searchList);
        Assert.assertEquals(result.size(), 12);
        result.forEach(e -> Assert.assertTrue(searchList.contains(e.getYear())));
    }

    @Test
    public void TestYearFilteringNoResults() {
        List<Integer> searchList = Stream.of(1990, 2050).collect(Collectors.toList());
        List<UpdateCouponEntity> result = CouponFilterUtils.filterByYear(couponV2List, searchList);
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void TestValidityTypeFiltering() {
        List<Validity> searchList = Stream.of(Validity.MONTHLY).collect(Collectors.toList());
        List<UpdateCouponEntity> result = CouponFilterUtils.filterByValidity(couponV2List, searchList);
        Assert.assertEquals(result.size(), 6);
        result.forEach(e -> Assert.assertTrue(searchList.contains(e.getValidity())));
    }

    @Test
    public void TestValidityTypeFilteringNoResults() {
        List<Validity> searchList = Stream.of(Validity.FIVE_MONTHS).collect(Collectors.toList());
        List<UpdateCouponEntity> result = CouponFilterUtils.filterByValidity(couponV2List, searchList);
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void TestSellTypeFiltering() {
        List<SellType> searchList = Stream.of(SellType.CARD).collect(Collectors.toList());
        List<UpdateCouponEntity> result = CouponFilterUtils.filterBySellType(couponV2List, searchList);
        Assert.assertEquals(result.size(), 6);
        result.forEach(e -> Assert.assertTrue(searchList.contains(e.getSellType())));
    }

    @Test
    public void TestSellTypeFilteringNoResults() {
        List<SellType> searchList = Stream.of(SellType.COUPON).collect(Collectors.toList());
        List<UpdateCouponEntity> result = CouponFilterUtils.filterBySellType(couponV2List, searchList);
        Assert.assertEquals(result.size(), 0);
    }

}
