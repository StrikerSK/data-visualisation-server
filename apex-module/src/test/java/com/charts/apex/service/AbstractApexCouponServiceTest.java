package com.charts.apex.service;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.enums.types.PersonType;
import com.charts.api.coupon.enums.types.SellType;
import com.charts.api.coupon.enums.types.Validity;
import com.charts.api.coupon.repository.JpaCouponV2Repository;
import com.charts.api.coupon.service.CouponV2Service;
import com.charts.general.entity.enums.types.Months;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractApexCouponServiceTest extends AbstractTestNGSpringContextTests {

    @Mock
    protected JpaCouponV2Repository couponV2Repository = Mockito.mock(JpaCouponV2Repository.class);

    @Mock
    protected CouponV2Service couponService = Mockito.mock(CouponV2Service.class, Mockito.CALLS_REAL_METHODS);

    @InjectMocks
    protected ApexCouponService apexCouponService;

    protected static AutoCloseable closeable;

    @BeforeClass
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);

        UpdateCouponEntity couponEntity1 = UpdateCouponEntity.builder().id(123L).value(100).month(Months.JANUARY).year(2015).validity(Validity.FIVE_MONTHS).sellType(SellType.COUPON).personType(PersonType.ADULT).build();
        UpdateCouponEntity couponEntity2 = UpdateCouponEntity.builder().id(123L).value(200).month(Months.JANUARY).year(2016).validity(Validity.MONTHLY).sellType(SellType.COUPON).personType(PersonType.ADULT).build();

        UpdateCouponEntity couponEntity3 = UpdateCouponEntity.builder().id(123L).value(300).month(Months.MARCH).year(2016).validity(Validity.FIVE_MONTHS).sellType(SellType.CARD).personType(PersonType.ADULT).build();
        UpdateCouponEntity couponEntity4 = UpdateCouponEntity.builder().id(123L).value(400).month(Months.MARCH).year(2015).validity(Validity.MONTHLY).sellType(SellType.ESHOP).personType(PersonType.ADULT).build();

        UpdateCouponEntity couponEntity5 = UpdateCouponEntity.builder().id(123L).value(500).month(Months.FEBRUARY).year(2016).validity(Validity.YEARLY).sellType(SellType.CARD).personType(PersonType.SENIOR).build();
        UpdateCouponEntity couponEntity6 = UpdateCouponEntity.builder().id(123L).value(600).month(Months.FEBRUARY).year(2015).validity(Validity.MONTHLY).sellType(SellType.ESHOP).personType(PersonType.SENIOR).build();

        List<UpdateCouponEntity> couponList = new ArrayList<>();
        couponList.add(couponEntity1);
        couponList.add(couponEntity2);
        couponList.add(couponEntity3);
        couponList.add(couponEntity4);
        couponList.add(couponEntity5);
        couponList.add(couponEntity6);

        Mockito.when(couponService.findCouponEntities(Mockito.any())).thenReturn(couponList);
        apexCouponService = new ApexCouponService(couponService);
    }

    @AfterClass
    public void tearDown() throws Exception {
        closeable.close();
    }

}