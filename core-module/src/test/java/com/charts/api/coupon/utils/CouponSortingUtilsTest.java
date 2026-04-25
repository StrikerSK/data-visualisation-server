package com.charts.api.coupon.utils;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.general.entity.enums.types.Months;
import com.charts.general.utils.AbstractSortingUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponSortingUtilsTest {

    private final Map<Months, List<UpdateCouponEntity>> monthMap = new HashMap<>();

    @BeforeClass
    public void setUp() {
        monthMap.put(Months.DECEMBER, Collections.emptyList());
        monthMap.put(Months.JUNE, Collections.emptyList());
        monthMap.put(Months.MARCH, Collections.emptyList());
    }

    @Test
    public void testSortByMonth() {
        List<Months> result = new ArrayList<>(AbstractSortingUtils.sortByOrderValue(monthMap).keySet());
        Assert.assertEquals(result.size(), 3);
        Assert.assertEquals(result.get(0), Months.MARCH);
        Assert.assertEquals(result.get(1), Months.JUNE);
        Assert.assertEquals(result.get(2), Months.DECEMBER);
    }

}
