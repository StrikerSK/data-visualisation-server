package com.charts.general.utils;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.utils.CouponGroupingUtils;
import com.charts.general.entity.coupon.AbstractCouponTest;
import com.charts.general.entity.enums.types.Months;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupingUtilsTest extends AbstractCouponTest {

    @Test
    public void testAggregateGroupsSumReturnsLongValuesInOrder() {
        Map<Months, Long> aggregatedValues = AbstractGroupingUtils.aggregateGroupsSum(
                couponV2List,
                CouponGroupingUtils::groupByMonth
        );

        Assert.assertEquals(new ArrayList<>(aggregatedValues.keySet()).get(0), Months.JANUARY);
        Assert.assertEquals(aggregatedValues.get(Months.JANUARY), Long.valueOf(0));
        Assert.assertEquals(aggregatedValues.get(Months.MARCH), Long.valueOf(21000));
        Assert.assertEquals(aggregatedValues.get(Months.DECEMBER), Long.valueOf(2100));
    }

    @Test
    public void testConvertMapKeysToStringPreservesOrder() {
        Map<String, Long> converted = AbstractGroupingUtils.convertMapKeysToString(
                AbstractGroupingUtils.aggregateGroupsSum(couponV2List, CouponGroupingUtils::groupByMonth)
        );

        List<String> keys = new ArrayList<>(converted.keySet());
        Assert.assertEquals(keys.get(0), Months.JANUARY.getValue());
        Assert.assertEquals(keys.get(2), Months.MARCH.getValue());
        Assert.assertEquals(converted.get(Months.MARCH.getValue()), Long.valueOf(21000));
    }
}
