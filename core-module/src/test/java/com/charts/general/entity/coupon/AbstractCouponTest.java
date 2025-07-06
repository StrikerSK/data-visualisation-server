package com.charts.general.entity.coupon;

import com.charts.api.coupon.entity.v1.CouponEntityV1;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.repository.JpaCouponV2Repository;
import com.charts.api.coupon.utils.CouponConvertor;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.entity.enums.types.Months;
import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.api.coupon.entity.enums.types.Validity;
import com.charts.api.coupon.repository.JpaCouponRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

public abstract class AbstractCouponTest extends AbstractTestNGSpringContextTests {

    protected CouponEntityV1 couponEntityV11;
    protected CouponEntityV1 couponEntityV12;
    protected CouponEntityV1 couponEntityV13;

    protected List<UpdateCouponEntity> singleEntryList;
    protected List<UpdateCouponEntity> couponV2List;
    protected List<CouponEntityV1> couponV1List;

    protected AutoCloseable closeable;

    @Mock
    protected JpaCouponRepository formerCouponRepository = Mockito.mock(JpaCouponRepository.class);

    @Mock
    protected JpaCouponV2Repository couponRepository = Mockito.mock(JpaCouponV2Repository.class);

    @BeforeClass
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        couponEntityV11 = CouponEntityV1.builder()
                .portable(100)
                .seniors(200)
                .adults(300)
                .students(400)
                .junior(500)
                .children(600)
                .type(SellType.ESHOP)
                .validity(Validity.YEARLY)
                .code("032000")
                .month(Months.AUGUST)
                .year(2000)
                .build();

        couponEntityV12 = CouponEntityV1.builder()
                .portable(1000)
                .seniors(2000)
                .adults(3000)
                .students(4000)
                .junior(5000)
                .children(6000)
                .type(SellType.CARD)
                .validity(Validity.MONTHLY)
                .code("082020")
                .month(Months.MARCH)
                .year(2020)
                .build();

        couponEntityV13 = CouponEntityV1.builder()
                .portable(100)
                .seniors(200)
                .adults(300)
                .students(400)
                .junior(500)
                .children(600)
                .type(SellType.ESHOP)
                .validity(Validity.YEARLY)
                .code("122015")
                .month(Months.DECEMBER)
                .year(2015)
                .build();

        singleEntryList = CouponConvertor.convertCouponEntity(couponEntityV11);
        couponV1List = Stream.of(couponEntityV11, couponEntityV12, couponEntityV13).collect(Collectors.toList());
        couponV2List = CouponConvertor.convertCouponEntity(couponV1List);

        when(formerCouponRepository.findAll()).thenReturn(couponV1List);
        when(couponRepository.findAll()).thenReturn(couponV2List);
    }

    @AfterClass
    public void tearDown() throws Exception {
        closeable.close();
    }

    protected <T extends IEnum, R> boolean isSorted(Map<T, List<R>> map) {
        List<Integer> orderValues = map.keySet().stream().map(IEnum::getOrderValue).collect(Collectors.toList());
        for (int i = 0; i < orderValues.size() - 1; i++) {
            if (orderValues.get(i) > orderValues.get(i + 1))
                return false;
        }
        return true;
    }

}
