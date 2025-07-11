package com.charts.apex.service;

import com.charts.apex.entity.ApexObject;
import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.charts.api.coupon.entity.enums.types.PersonType.*;
import static com.charts.api.coupon.entity.enums.types.SellType.*;
import static com.charts.api.coupon.entity.enums.types.Validity.*;
import static com.charts.general.entity.constants.EnumerationConstants.MONTH_VALUES;
import static com.charts.general.entity.enums.types.Months.*;
import static org.mockito.Mockito.when;

public class ApexCouponServiceTest {

    @Mock
    private CouponV2Service couponV2Service;

    @InjectMocks
    private ApexCouponService apexCouponService;

    private AutoCloseable closeable;
    private final CouponsParameters defaultCouponParameters = new CouponsParameters(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

    @BeforeClass
    public void setup() {
        UpdateCouponEntity couponEntity1 = UpdateCouponEntity.builder().id(123L).value(100).month(JANUARY).year(2015).validity(FIVE_MONTHS).sellType(COUPON).personType(ADULT).build();
        UpdateCouponEntity couponEntity2 = UpdateCouponEntity.builder().id(123L).value(200).month(JANUARY).year(2016).validity(MONTHLY).sellType(ESHOP).personType(ADULT).build();

        UpdateCouponEntity couponEntity3 = UpdateCouponEntity.builder().id(123L).value(300).month(MARCH).year(2016).validity(FIVE_MONTHS).sellType(CARD).personType(ADULT).build();
        UpdateCouponEntity couponEntity4 = UpdateCouponEntity.builder().id(123L).value(400).month(MARCH).year(2015).validity(MONTHLY).sellType(COUPON).personType(ADULT).build();

        UpdateCouponEntity couponEntity5 = UpdateCouponEntity.builder().id(123L).value(500).month(FEBRUARY).year(2016).validity(YEARLY).sellType(CARD).personType(SENIOR).build();
        UpdateCouponEntity couponEntity6 = UpdateCouponEntity.builder().id(123L).value(600).month(FEBRUARY).year(2015).validity(MONTHLY).sellType(ESHOP).personType(SENIOR).build();

        UpdateCouponEntity couponEntity7 = UpdateCouponEntity.builder().id(123L).value(100).month(JANUARY).year(2015).validity(FIVE_MONTHS).sellType(COUPON).personType(ADULT).build();
        UpdateCouponEntity couponEntity8 = UpdateCouponEntity.builder().id(123L).value(200).month(JANUARY).year(2016).validity(MONTHLY).sellType(ESHOP).personType(ADULT).build();

        UpdateCouponEntity couponEntity9 = UpdateCouponEntity.builder().id(123L).value(300).month(MARCH).year(2016).validity(FIVE_MONTHS).sellType(CARD).personType(ADULT).build();
        UpdateCouponEntity couponEntity10 = UpdateCouponEntity.builder().id(123L).value(400).month(MARCH).year(2015).validity(MONTHLY).sellType(COUPON).personType(ADULT).build();

        UpdateCouponEntity couponEntity11 = UpdateCouponEntity.builder().id(123L).value(500).month(FEBRUARY).year(2016).validity(YEARLY).sellType(CARD).personType(SENIOR).build();
        UpdateCouponEntity couponEntity12 = UpdateCouponEntity.builder().id(123L).value(600).month(FEBRUARY).year(2015).validity(MONTHLY).sellType(ESHOP).personType(SENIOR).build();

        List<UpdateCouponEntity> couponList = List.of(
                couponEntity1, couponEntity2, couponEntity3,
                couponEntity4, couponEntity5, couponEntity6,
                couponEntity7, couponEntity8, couponEntity9,
                couponEntity10, couponEntity11, couponEntity12
        );

        closeable = MockitoAnnotations.openMocks(this);
        when(couponV2Service.findCouponEntities(Mockito.any())).thenReturn(couponList);
    }

    @AfterClass
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void retrieveData_yearlyPerPersonType() {
        List<ApexObject> result = apexCouponService.getCouponData(CouponFunctionUtils.YEAR_GROUP, CouponFunctionUtils.PERSON_GROUP, defaultCouponParameters);
        List<String> resultValues = result.stream().map(ApexObject::getName).collect(Collectors.toList());
        Assert.assertEquals(resultValues, List.of("2015", "2016"));

        ApexObject apexObject1 = getApexObject("2015", result);
        Assert.assertEquals(getApexObjectSum("2015", result), 2200);
        Assert.assertEquals(apexObject1.getValues().size(), 2);
        Assert.assertEquals(apexObject1.getValues(), List.of(1000, 1200));

        ApexObject apexObject2 = getApexObject("2016", result);
        Assert.assertEquals(getApexObjectSum("2016", result), 2000);
        Assert.assertEquals(apexObject2.getValues().size(), 2);
        Assert.assertEquals(apexObject2.getValues(), List.of(1000, 1000));
    }

    @Test
    public void retrieveData_yearlyPerSellType() {
        List<ApexObject> result = apexCouponService.getCouponData(CouponFunctionUtils.YEAR_GROUP, CouponFunctionUtils.SELL_GROUP, defaultCouponParameters);
        List<String> resultValues = result.stream().map(ApexObject::getName).collect(Collectors.toList());
        Assert.assertEquals(resultValues, List.of("2015", "2016"));

        ApexObject apexObject1 = getApexObject("2015", result);
        Assert.assertEquals(getApexObjectSum("2015", result), 2200);
        Assert.assertEquals(apexObject1.getValues().size(), 2);
        Assert.assertEquals(apexObject1.getValues(), List.of(1000, 1200));

        ApexObject apexObject2 = getApexObject("2016", result);
        Assert.assertEquals(getApexObjectSum("2016", result), 2000);
        Assert.assertEquals(apexObject2.getValues().size(), 2);
        Assert.assertEquals(apexObject2.getValues(), List.of(1600, 400));
    }

    @Test
    public void retrieveData_yearlyPerValidity() {
        List<ApexObject> result = apexCouponService.getCouponData(CouponFunctionUtils.YEAR_GROUP, CouponFunctionUtils.VALIDITY_GROUP, defaultCouponParameters);
        List<String> resultValues = result.stream().map(ApexObject::getName).collect(Collectors.toList());
        Assert.assertEquals(resultValues, List.of("2015", "2016"));

        ApexObject apexObject1 = getApexObject("2015", result);
        Assert.assertEquals(getApexObjectSum("2015", result), 2200);
        Assert.assertEquals(apexObject1.getValues().size(), 2);
        Assert.assertEquals(apexObject1.getValues(), List.of(2000, 200));

        ApexObject apexObject2 = getApexObject("2016", result);
        Assert.assertEquals(getApexObjectSum("2016", result), 2000);
        Assert.assertEquals(apexObject2.getValues().size(), 3);
        Assert.assertEquals(apexObject2.getValues(), List.of(400, 600, 1000));
    }

    @Test
    public void retrieveData_monthly() {
        List<ApexObject> result = apexCouponService.getCouponData(CouponFunctionUtils.MONTH_GROUP, CouponFunctionUtils.PERSON_GROUP, defaultCouponParameters);
        List<String> resultValues = result.stream().map(ApexObject::getName).collect(Collectors.toList());
        Assert.assertEquals(resultValues, MONTH_VALUES);

        Assert.assertEquals(getApexObjectSum(JANUARY.getValue(), result), 600);
        Assert.assertEquals(getApexObjectSum(FEBRUARY.getValue(), result), 2200);
        Assert.assertEquals(getApexObjectSum(MARCH.getValue(), result), 1400);
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
    public void retrieveData_personPerYear() {
        List<ApexObject> result = apexCouponService.getCouponData(CouponFunctionUtils.PERSON_GROUP, CouponFunctionUtils.YEAR_GROUP, defaultCouponParameters);
        List<String> resultValues = result.stream().map(ApexObject::getName).collect(Collectors.toList());
        Assert.assertEquals(resultValues, List.of(ADULT.getValue(), SENIOR.getValue()));

        ApexObject apexObject1 = getApexObject(ADULT.getValue(), result);
        Assert.assertEquals(getApexObjectSum(ADULT.getValue(), result), 2000);
        Assert.assertEquals(apexObject1.getValues().size(), 2);
        Assert.assertEquals(apexObject1.getValues(), List.of(1000, 1000));

        ApexObject apexObject2 = getApexObject(SENIOR.getValue(), result);
        Assert.assertEquals(getApexObjectSum(SENIOR.getValue(), result), 2200);
        Assert.assertEquals(apexObject2.getValues().size(), 2);
        Assert.assertEquals(apexObject2.getValues(), List.of(1200, 1000));
    }

    @Test
    public void retrieveData_validityPerPerson() {
        List<ApexObject> result = apexCouponService.getCouponData(CouponFunctionUtils.VALIDITY_GROUP, CouponFunctionUtils.PERSON_GROUP, defaultCouponParameters);
        List<String> resultValues = result.stream().map(ApexObject::getName).collect(Collectors.toList());
        Assert.assertEquals(resultValues, List.of(MONTHLY.getValue(), FIVE_MONTHS.getValue(), YEARLY.getValue()));

        ApexObject apexObject1 = getApexObject(MONTHLY.getValue(), result);
        Assert.assertEquals(getApexObjectSum(MONTHLY.getValue(), result), 2400);
        Assert.assertEquals(apexObject1.getValues().size(), 2);
        Assert.assertEquals(apexObject1.getValues(), List.of(1200, 1200));

        ApexObject apexObject2 = getApexObject(FIVE_MONTHS.getValue(), result);
        Assert.assertEquals(getApexObjectSum(FIVE_MONTHS.getValue(), result), 800);
        Assert.assertEquals(apexObject2.getValues().size(), 1);
        Assert.assertEquals(apexObject2.getValues(), List.of(800));

        ApexObject apexObject3 = getApexObject(YEARLY.getValue(), result);
        Assert.assertEquals(getApexObjectSum(YEARLY.getValue(), result), 1000);
        Assert.assertEquals(apexObject3.getValues().size(), 1);
        Assert.assertEquals(apexObject3.getValues(), List.of(1000));
    }

    @Test
    public void retrieveData_sellTypePerPerson() {
        List<ApexObject> result = apexCouponService.getCouponData(CouponFunctionUtils.SELL_GROUP, CouponFunctionUtils.PERSON_GROUP, defaultCouponParameters);
        List<String> resultValues = result.stream().map(ApexObject::getName).collect(Collectors.toList());
        Assert.assertEquals(resultValues, List.of(CARD.getValue(), COUPON.getValue(), ESHOP.getValue()));

        ApexObject couponGroup = getApexObject(COUPON.getValue(), result);
        Assert.assertEquals(getApexObjectSum(COUPON.getValue(), result), 1000);
        Assert.assertEquals(couponGroup.getValues().size(), 1);
        Assert.assertEquals(couponGroup.getValues(), List.of(1000));

        ApexObject eshopGroup = getApexObject(ESHOP.getValue(), result);
        Assert.assertEquals(getApexObjectSum(CARD.getValue(), result), 1600);
        Assert.assertEquals(eshopGroup.getValues().size(), 2);
        Assert.assertEquals(eshopGroup.getValues(), List.of(400, 1200));

        ApexObject cardGroup = getApexObject(CARD.getValue(), result);
        Assert.assertEquals(getApexObjectSum(CARD.getValue(), result), 1600);
        Assert.assertEquals(cardGroup.getValues().size(), 2);
        Assert.assertEquals(cardGroup.getValues(), List.of(600, 1000));
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

}