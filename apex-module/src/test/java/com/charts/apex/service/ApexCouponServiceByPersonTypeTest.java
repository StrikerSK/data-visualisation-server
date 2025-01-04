package com.charts.apex.service;

import com.charts.apex.entity.ApexObject;
import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.api.coupon.enums.types.PersonType;
import com.charts.api.coupon.enums.types.SellType;
import com.charts.api.coupon.enums.types.Validity;
import com.charts.general.entity.enums.types.Months;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApexCouponServiceByPersonTypeTest extends AbstractApexCouponServiceTest {

    @Test
    public void generalDataRetrieval() {
        CouponsParameters couponsParameters = new CouponsParameters(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        List<ApexObject> result = apexCouponService.getMonthlyDataByPersonType(couponsParameters);

        assertEquals(6, result.size());
        assertEquals(1000, result.get(0).getValues().stream().reduce(0, Integer::sum));
    }

    @Test
    public void retrieveDataByValidityType() {
        List<String> validityType = new ArrayList<>();
        validityType.add(Validity.FIVE_MONTHS.getValue());

        CouponsParameters couponsParameters = new CouponsParameters(validityType, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        List<ApexObject> result = apexCouponService.getMonthlyDataByPersonType(couponsParameters);

        assertEquals(6, result.size());
        assertEquals(400, result.get(0).getValues().stream().reduce(0, Integer::sum));
    }

    @Test
    public void retrieveDataBySellType() {
        List<String> sellTypes = new ArrayList<>();
        sellTypes.add(SellType.ESHOP.getValue());

        CouponsParameters couponsParameters = new CouponsParameters(new ArrayList<>(), sellTypes, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        List<ApexObject> result = apexCouponService.getMonthlyDataByPersonType(couponsParameters);

        assertEquals(6, result.size());
        assertEquals(400, result.get(0).getValues().stream().reduce(0, Integer::sum));
    }

    @Test
    public void retrieveDataByMonth() {
        List<String> months = new ArrayList<>();
        months.add(Months.MARCH.getValue());

        CouponsParameters couponsParameters = new CouponsParameters(new ArrayList<>(), new ArrayList<>(), months, new ArrayList<>(), new ArrayList<>());
        List<ApexObject> result = apexCouponService.getMonthlyDataByPersonType(couponsParameters);

        assertEquals(6, result.size());
        assertEquals(700, result.get(0).getValues().stream().reduce(0, Integer::sum));
    }

    @Test
    public void retrieveDataByYear() {
        List<Integer> years = new ArrayList<>();
        years.add(2016);

        CouponsParameters couponsParameters = new CouponsParameters(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), years, new ArrayList<>());
        List<ApexObject> result = apexCouponService.getMonthlyDataByPersonType(couponsParameters);

        assertEquals(6, result.size());
        assertEquals(500, result.get(0).getValues().stream().reduce(0, Integer::sum));
    }

    @Test
    public void retrieveDataByPersonType() {
        List<String> personTypes = new ArrayList<>();
        personTypes.add(PersonType.SENIOR.getValue());

        CouponsParameters couponsParameters = new CouponsParameters(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), personTypes);
        List<ApexObject> result = apexCouponService.getMonthlyDataByPersonType(couponsParameters);

        assertEquals(1, result.size());
        assertEquals(1100, result.get(0).getValues().stream().reduce(0, Integer::sum));
    }

}