package com.charts.api.coupon.utils;

import com.charts.api.coupon.entity.enums.types.PersonType;
import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.general.entity.coupon.AbstractCouponTest;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.entity.enums.types.EnumAdapter;
import com.charts.general.entity.enums.types.Months;
import com.charts.general.exception.InvalidParameterException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CouponFunctionUtilsTest extends AbstractCouponTest {

    @Test
    public void testCreateGrouping_person() {
        Map<IEnum, List<UpdateCouponEntity>> groupedValues = CouponFunctionUtils.createGrouping(CouponFunctionUtils.PERSON_GROUP).apply(couponV2List);

        Assert.assertEquals(groupedValues.size(), 6);
        Assert.assertTrue(groupedValues.containsKey(PersonType.PORTABLE));
        Assert.assertTrue(groupedValues.containsKey(PersonType.CHILDREN));
    }

    @Test
    public void testCreateGrouping_sell() {
        Map<IEnum, List<UpdateCouponEntity>> groupedValues = CouponFunctionUtils.createGrouping(CouponFunctionUtils.SELL_GROUP).apply(couponV2List);

        Assert.assertEquals(groupedValues.size(), 2);
        Assert.assertTrue(groupedValues.containsKey(SellType.CARD));
        Assert.assertTrue(groupedValues.containsKey(SellType.ESHOP));
    }

    @Test
    public void testCreateGrouping_monthsPreservesOrderAndEmptyBuckets() {
        Map<IEnum, List<UpdateCouponEntity>> groupedValues = CouponFunctionUtils.createGrouping(CouponFunctionUtils.MONTH_GROUP).apply(couponV2List);

        Assert.assertEquals(groupedValues.size(), Months.values().length);
        Assert.assertEquals(groupedValues.keySet().stream().findFirst().orElseThrow(), Months.JANUARY);
        Assert.assertEquals(groupedValues.get(Months.JANUARY).size(), 0);
        Assert.assertEquals(groupedValues.get(Months.MARCH).size(), 6);
    }

    @Test
    public void testCreateGrouping_yearUsesEnumAdapter() {
        Map<IEnum, List<UpdateCouponEntity>> groupedValues = CouponFunctionUtils.createGrouping(CouponFunctionUtils.YEAR_GROUP).apply(couponV2List);

        Assert.assertTrue(groupedValues.keySet().stream().allMatch(EnumAdapter.class::isInstance));
        Assert.assertTrue(groupedValues.containsKey(new EnumAdapter(2020)));
        Assert.assertEquals(groupedValues.get(new EnumAdapter(2020)).size(), 6);
    }

    @Test
    public void testCreateGrouping_invalidGroup() {
        InvalidParameterException exception = Assert.expectThrows(
                InvalidParameterException.class,
                () -> CouponFunctionUtils.createGrouping("invalid")
        );

        Assert.assertTrue(exception.getMessage().contains("Unknown group name"));
    }

    @Test
    public void testCreateGrouping_canBePassedToIEnumFunctionReference() {
        Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> groupingFunction =
                CouponFunctionUtils.createGrouping(CouponFunctionUtils.VALIDITY_GROUP);

        Assert.assertEquals(groupingFunction.apply(couponV2List).size(), 2);
    }
}
