package com.charts.apex.service;

import com.charts.apex.entity.ApexObject;
import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.enums.types.PersonType;
import com.charts.api.coupon.enums.types.SellType;
import com.charts.api.coupon.enums.types.Validity;
import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.coupon.utils.CouponFunctionUtils;
import com.charts.general.entity.enums.types.Months;
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

import static org.mockito.Mockito.when;

public class DummyTest {

    @Mock
    private CouponV2Service couponV2Service;

    @InjectMocks
    private ApexCouponService apexCouponService;

    private AutoCloseable closeable;
    private List<UpdateCouponEntity> couponList;
    private final CouponsParameters couponsParameters = new CouponsParameters(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());


    @BeforeClass
    public void setup() {
        UpdateCouponEntity couponEntity1 = UpdateCouponEntity.builder().id(123L).value(100).month(Months.JANUARY).year(2015).validity(Validity.FIVE_MONTHS).sellType(SellType.COUPON).personType(PersonType.ADULT).build();
        UpdateCouponEntity couponEntity2 = UpdateCouponEntity.builder().id(123L).value(200).month(Months.JANUARY).year(2016).validity(Validity.MONTHLY).sellType(SellType.COUPON).personType(PersonType.ADULT).build();

        UpdateCouponEntity couponEntity3 = UpdateCouponEntity.builder().id(123L).value(300).month(Months.MARCH).year(2016).validity(Validity.FIVE_MONTHS).sellType(SellType.CARD).personType(PersonType.ADULT).build();
        UpdateCouponEntity couponEntity4 = UpdateCouponEntity.builder().id(123L).value(400).month(Months.MARCH).year(2015).validity(Validity.MONTHLY).sellType(SellType.ESHOP).personType(PersonType.ADULT).build();

        UpdateCouponEntity couponEntity5 = UpdateCouponEntity.builder().id(123L).value(500).month(Months.FEBRUARY).year(2016).validity(Validity.YEARLY).sellType(SellType.CARD).personType(PersonType.SENIOR).build();
        UpdateCouponEntity couponEntity6 = UpdateCouponEntity.builder().id(123L).value(600).month(Months.FEBRUARY).year(2015).validity(Validity.MONTHLY).sellType(SellType.ESHOP).personType(PersonType.SENIOR).build();

        couponList = new ArrayList<>();
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
    public void generalDataRetrieval() {
        List<ApexObject> result = apexCouponService.getCouponData(CouponFunctionUtils.MONTH_GROUP, CouponFunctionUtils.PERSON_GROUP, couponsParameters);
        Assert.assertEquals(result.size(), 12);
        Assert.assertEquals(result.get(0).getValues().stream().reduce(0, Integer::sum), 1000);
    }

    @AfterClass
    public void tearDown() throws Exception {
        closeable.close();
    }

    // Add more test methods for other methods in ApexCouponService
}