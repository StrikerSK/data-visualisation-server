package com.charts.general.utils;

import com.charts.general.entity.GroupingEntity;
import com.charts.general.entity.enums.types.EnumAdapter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class GroupingEntityUtilsTest {

    @Test
    public void testFromIntegers() {
        List<GroupingEntity<EnumAdapter>> values = GroupingEntityUtils.fromIntegers(List.of(new GroupingEntity<>(2024, 10L)));

        Assert.assertEquals(values.size(), 1);
        Assert.assertEquals(values.get(0).getKey().getValue(), "2024");
        Assert.assertEquals(values.get(0).getValue(), Long.valueOf(10));
    }

    @Test
    public void testFromBooleans() {
        List<GroupingEntity<EnumAdapter>> values = GroupingEntityUtils.fromBooleans(List.of(new GroupingEntity<>(Boolean.TRUE, 7L)));

        Assert.assertEquals(values.size(), 1);
        Assert.assertEquals(values.get(0).getKey().getValue(), "true");
        Assert.assertEquals(values.get(0).getKey().getOrderValue(), Integer.valueOf(1));
        Assert.assertEquals(values.get(0).getValue(), Long.valueOf(7));
    }
}
