package com.charts.api.coupon.utils;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.entity.enums.types.PersonType;
import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.api.coupon.entity.enums.types.Validity;
import com.charts.general.entity.AbstractUpdateEntity;
import com.charts.general.entity.coupon.AbstractCouponTest;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.entity.enums.types.Months;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CouponGroupingUtilsTest extends AbstractCouponTest {

    @Test
    public void testGrouping_personType() {
        Map<PersonType, List<UpdateCouponEntity>> map = CouponGroupingUtils.groupByPersonType(couponV2List);
        Assert.assertEquals(map.size(), 6);
        Assert.assertTrue(isSorted(map));

        assertCoupon(map, PersonType.PORTABLE, 1200);
        assertCoupon(map, PersonType.SENIOR, 2400);
        assertCoupon(map, PersonType.ADULT, 3600);
        assertCoupon(map, PersonType.STUDENT, 4800);
        assertCoupon(map, PersonType.JUNIOR, 6000);
        assertCoupon(map, PersonType.CHILDREN, 7200);
    }

    @Test
    public void testGrouping_validity() {
        Map<Validity, List<UpdateCouponEntity>> map = CouponGroupingUtils.groupByValidity(couponV2List);
        Assert.assertEquals(map.size(), 2);
        Assert.assertTrue(isSorted(map));

        assertCoupon(map, Validity.MONTHLY, 21000);
        assertCoupon(map, Validity.YEARLY, 4200);
    }

    @Test
    public void testGrouping_sellType() {
        Map<SellType, List<UpdateCouponEntity>> map = CouponGroupingUtils.groupBySellType(couponV2List);
        Assert.assertEquals(map.size(), 2);
        Assert.assertTrue(isSorted(map));

        assertCoupon(map, SellType.CARD, 21000);
        assertCoupon(map, SellType.ESHOP, 4200);
    }

    @Test
    public void testGrouping_months() {
        Map<Months, List<UpdateCouponEntity>> map = CouponGroupingUtils.groupByMonth(couponV2List);
        Assert.assertEquals(map.size(), 12);
        Assert.assertTrue(isSorted(map));

        assertCoupon(map, Months.JANUARY, 0);
        assertCoupon(map, Months.FEBRUARY, 0);
        assertCoupon(map, Months.MARCH, 21000);
        assertCoupon(map, Months.APRIL, 0);
        assertCoupon(map, Months.MAY, 0);
        assertCoupon(map, Months.JUNE, 0);
        assertCoupon(map, Months.JULY, 0);
        assertCoupon(map, Months.AUGUST, 2100);
        assertCoupon(map, Months.SEPTEMBER, 0);
        assertCoupon(map, Months.OCTOBER, 0);
        assertCoupon(map, Months.NOVEMBER, 0);
        assertCoupon(map, Months.DECEMBER, 2100);
    }

    private <T extends IEnum> void assertCoupon(Map<T, List<UpdateCouponEntity>> entries, T key, Integer expSum) {
        Assert.assertEquals(entries.get(key).stream().map(AbstractUpdateEntity::getValue).reduce(0, Integer::sum), expSum);
    }

}
