package com.charts.general.entity.enums;

import com.charts.api.coupon.entity.enums.types.SellType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.charts.api.coupon.constants.EnumerationCouponConstants.*;

public class EnumUtilTest {

    @Test
    public void testFromValue_ValidLabel() {
        Class<SellType> enumClass = SellType.class;
        Optional<SellType> result = EnumUtils.getValue(enumClass, CHIP_CARD);

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get(), SellType.CARD);
    }

    @Test
    public void testFromValue_InvalidLabel() {
        Class<SellType> enumClass = SellType.class;
        String label = "Invalid Label";
        Assert.assertThrows(IllegalArgumentException.class, () -> EnumUtils.fromValue(enumClass, label));
    }

    @Test
    public void testFromValue_NullLabel() {
        Class<SellType> enumClass = SellType.class;
        Assert.assertThrows(IllegalArgumentException.class, () -> EnumUtils.fromValue(enumClass, null));
    }

    @Test
    public void testGetValueList() {
        Class<SellType> enumClass = SellType.class;
        List<SellType> result = EnumUtils.getValueList(enumClass);
        Assert.assertEquals(result.size(), SellType.values().length);

        SellType sellType = result.get(0);
        Assert.assertEquals(sellType, SellType.CARD);
        Assert.assertEquals(sellType.getOrderValue(), 1);
    }

    @Test
    public void testGetStringValues() {
        List<String> enums = Arrays.asList(CHIP_CARD, E_SHOP);
        List<SellType> actEnums = EnumUtils.getValueList(enums, SellType.class);

        Assert.assertEquals(actEnums.size(), 2);
        Assert.assertTrue(actEnums.contains(SellType.CARD));
        Assert.assertTrue(actEnums.contains(SellType.ESHOP));
        Assert.assertFalse(actEnums.contains(SellType.COUPON));
    }

    @Test
    public void testGetAllStringValues() {
        List<String> actEnums = EnumUtils.getStringValues(SellType.class);
        List<String> expEnums = Arrays.asList(CHIP_CARD, PAPER_COUPON, E_SHOP);
        Assert.assertEquals(actEnums.size(), 3);
        Assert.assertEquals(actEnums, expEnums);
    }

}