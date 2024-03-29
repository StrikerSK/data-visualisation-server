package com.charts.general.entity.coupon;

import com.charts.general.entity.enums.SellType;
import com.charts.general.entity.enums.Validity;
import com.charts.general.utils.AbstractGroupingUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CouponListTest extends AbstractCouponTest {

    @Test
    public void TestMonthFiltering() {
        List<String> searchList = Stream.of("Marec", "December").collect(Collectors.toList());
        CouponList couponList = new CouponList(couponEntityList).filterByMonth(searchList);
        Assert.assertEquals(couponList.getCoupons().size(), 2);
        couponList.getCoupons().forEach(e -> Assert.assertTrue(searchList.contains(e.getMonth().getValue())));
    }

    @Test
    public void TestMonthFilteringNoResults() {
        List<String> searchList = Stream.of("October", "September").collect(Collectors.toList());
        CouponList couponList = new CouponList(couponEntityList).filterByMonth(searchList);
        Assert.assertEquals(couponList.getCoupons().size(), 0);
    }

    @Test
    public void TestYearFiltering() {
        List<Integer> searchList = Stream.of(2015, 2020).collect(Collectors.toList());
        CouponList couponList = new CouponList(couponEntityList).filterByYear(searchList);
        Assert.assertEquals(couponList.getCoupons().size(), 2);
        couponList.getCoupons().forEach(e -> Assert.assertTrue(searchList.contains(e.getYear())));
    }

    @Test
    public void TestYearFilteringNoResults() {
        List<Integer> searchList = Stream.of(1990, 2050).collect(Collectors.toList());
        CouponList couponList = new CouponList(couponEntityList).filterByYear(searchList);
        Assert.assertEquals(couponList.getCoupons().size(), 0);
    }

    @Test
    public void TestSellTypeFiltering() {
        List<SellType> searchList = Stream.of(SellType.CARD).collect(Collectors.toList());
        CouponList couponList = new CouponList(couponEntityList).filterByTypes(searchList);
        Assert.assertEquals(couponList.getCoupons().size(), 2);
        couponList.getCoupons().forEach(e -> Assert.assertTrue(searchList.contains(e.getType())));
    }

    @Test
    public void TestSellTypeFilteringNoResults() {
        List<SellType> searchList = Stream.of(SellType.COUPON).collect(Collectors.toList());
        CouponList couponList = new CouponList(couponEntityList).filterByTypes(searchList);
        Assert.assertEquals(couponList.getCoupons().size(), 0);
    }

    @Test
    public void TestValidityTypeFiltering() {
        List<Validity> searchList = Stream.of(Validity.MONTHLY).collect(Collectors.toList());
        CouponList couponList = new CouponList(couponEntityList).filterByValidity(searchList);
        Assert.assertEquals(couponList.getCoupons().size(), 1);
        couponList.getCoupons().forEach(e -> Assert.assertTrue(searchList.contains(e.getValidity())));
    }

    @Test
    public void TestValidityTypeFilteringNoResults() {
        List<Validity> searchList = Stream.of(Validity.FIVE_MONTHS).collect(Collectors.toList());
        CouponList couponList = new CouponList(couponEntityList).filterByValidity(searchList);
        Assert.assertEquals(couponList.getCoupons().size(), 0);
    }

}
