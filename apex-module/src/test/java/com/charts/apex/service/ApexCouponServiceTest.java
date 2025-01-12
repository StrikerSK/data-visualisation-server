package com.charts.apex.service;

import com.charts.apex.entity.ApexObject;
import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.enums.types.PersonType;
import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.coupon.utils.CouponFunctionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.charts.api.coupon.enums.types.SellType.*;
import static com.charts.api.coupon.enums.types.Validity.*;
import static com.charts.general.entity.enums.types.Months.*;
import static org.mockito.Mockito.when;

public class ApexCouponServiceTest {

    @Mock
    private CouponV2Service couponV2Service;

    @InjectMocks
    private ApexCouponService apexCouponService;

    private AutoCloseable closeable;
    private final CouponsParameters couponsParameters = new CouponsParameters(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());


    @BeforeClass
    public void setup() {
        UpdateCouponEntity couponEntity1 = UpdateCouponEntity.builder().id(123L).value(100).month(JANUARY).year(2015).validity(FIVE_MONTHS).sellType(COUPON).personType(PersonType.ADULT).build();
        UpdateCouponEntity couponEntity2 = UpdateCouponEntity.builder().id(123L).value(200).month(JANUARY).year(2016).validity(MONTHLY).sellType(ESHOP).personType(PersonType.ADULT).build();

        UpdateCouponEntity couponEntity3 = UpdateCouponEntity.builder().id(123L).value(300).month(MARCH).year(2016).validity(FIVE_MONTHS).sellType(CARD).personType(PersonType.ADULT).build();
        UpdateCouponEntity couponEntity4 = UpdateCouponEntity.builder().id(123L).value(400).month(MARCH).year(2015).validity(MONTHLY).sellType(COUPON).personType(PersonType.ADULT).build();

        UpdateCouponEntity couponEntity5 = UpdateCouponEntity.builder().id(123L).value(500).month(FEBRUARY).year(2016).validity(YEARLY).sellType(CARD).personType(PersonType.SENIOR).build();
        UpdateCouponEntity couponEntity6 = UpdateCouponEntity.builder().id(123L).value(600).month(FEBRUARY).year(2015).validity(MONTHLY).sellType(ESHOP).personType(PersonType.SENIOR).build();

        List<UpdateCouponEntity> couponList = new ArrayList<>();
        couponList.add(couponEntity1);
        couponList.add(couponEntity2);
        couponList.add(couponEntity3);
        couponList.add(couponEntity4);
        couponList.add(couponEntity5);
        couponList.add(couponEntity6);

        closeable = MockitoAnnotations.openMocks(this);
        when(couponV2Service.findCouponEntities(Mockito.any())).thenReturn(couponList);
    }

    @Test
    public void retrieveData_yearly() {
        List<ApexObject> result = apexCouponService.getCouponData(CouponFunctionUtils.YEAR_GROUP, CouponFunctionUtils.PERSON_GROUP, couponsParameters);
        Assert.assertEquals(result.size(), 2);

        ApexObject apexObject1 = getApexObject("2015", result);
        Assert.assertEquals(getApexObjectSum("2015", result), 1100);
        Assert.assertEquals(apexObject1.getValues().size(), 2);
        Assert.assertEquals(apexObject1.getValues(), List.of(500, 600));

        ApexObject apexObject2 = getApexObject("2016", result);
        Assert.assertEquals(getApexObjectSum("2016", result), 1000);
        Assert.assertEquals(apexObject2.getValues().size(), 2);
        Assert.assertEquals(apexObject2.getValues(), List.of(500, 500));
    }

    @Test
    public void retrieveData_monthly() {
        List<ApexObject> result = apexCouponService.getCouponData(CouponFunctionUtils.MONTH_GROUP, CouponFunctionUtils.PERSON_GROUP, couponsParameters);
        Assert.assertEquals(result.size(), 12);

        Assert.assertEquals(getApexObjectSum(JANUARY.getValue(), result), 300);
        Assert.assertEquals(getApexObjectSum(FEBRUARY.getValue(), result), 1100);
        Assert.assertEquals(getApexObjectSum(MARCH.getValue(), result), 700);
        Assert.assertEquals(getApexObjectSum(APRIL.getValue(), result), 0);
        Assert.assertEquals(getApexObjectSum(MAY.getValue(), result), 0);
        Assert.assertEquals(getApexObjectSum(JUNE.getValue(), result), 0);
        Assert.assertEquals(getApexObjectSum(JULY.getValue(), result), 0);
        Assert.assertEquals(getApexObjectSum(AUGUST.getValue(), result), 0);
        Assert.assertEquals(getApexObjectSum(SEPTEMBER.getValue(), result), 0);
        Assert.assertEquals(getApexObjectSum(OCTOBER.getValue(), result), 0);
        Assert.assertEquals(getApexObjectSum(NOVEMBER.getValue(), result), 0);
        Assert.assertEquals(getApexObjectSum(DECEMBER.getValue(), result), 0);
    }

    @Test
    public void retrieveData_validity() {
        List<ApexObject> result = apexCouponService.getCouponData(CouponFunctionUtils.VALIDITY_GROUP, CouponFunctionUtils.PERSON_GROUP, couponsParameters);
        Assert.assertEquals(result.size(), 3);

        Assert.assertEquals(getApexObjectSum(MONTHLY.getValue(), result), 1200);
        Assert.assertEquals(getApexObjectSum(FIVE_MONTHS.getValue(), result), 400);
        Assert.assertEquals(getApexObjectSum(YEARLY.getValue(), result), 500);
    }

    private Integer getApexObjectSum(String searchedValue, List<ApexObject> values) {
        return getApexObject(searchedValue, values).getValues().stream().reduce(0, Integer::sum);
    }

    private ApexObject getApexObject(String searchedValue, List<ApexObject> values) {
        return values.stream()
                .filter(o -> o.getName().equals(searchedValue))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("ApexObject not found for %s", searchedValue)));
    }

    @AfterClass
    public void tearDown() throws Exception {
        closeable.close();
    }
}