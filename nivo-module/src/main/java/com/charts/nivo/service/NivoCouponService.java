package com.charts.nivo.service;

import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.coupon.utils.CouponFunctionUtils;
import com.charts.api.coupon.utils.CouponGroupingUtils;
import com.charts.general.entity.GroupingEntity;
import com.charts.general.entity.enums.IEnum;
import com.charts.nivo.entity.NivoBubbleData;
import com.charts.nivo.entity.NivoLineData;
import com.charts.nivo.entity.NivoPieData;
import com.charts.nivo.utils.NivoConvertersUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.charts.api.coupon.utils.CouponFunctionUtils.MONTH_GROUP;
import static com.charts.api.coupon.utils.CouponFunctionUtils.PERSON_GROUP;
import static com.charts.api.coupon.utils.CouponFunctionUtils.SELL_GROUP;
import static com.charts.api.coupon.utils.CouponFunctionUtils.VALIDITY_GROUP;
import static com.charts.api.coupon.utils.CouponFunctionUtils.YEAR_GROUP;

@Service
public class NivoCouponService {

    private final CouponV2Service couponService;
    private final Map<String, Function<CouponsParameters, List<? extends GroupingEntity<?>>>> pieDataStrategies;

    public NivoCouponService(CouponV2Service couponService) {
        this.couponService = couponService;
        this.pieDataStrategies = Map.of(
                PERSON_GROUP, couponService::findByValidityAndGroupedByPersonType,
                MONTH_GROUP, couponService::findByValidityAndGroupedByMonth,
                SELL_GROUP, couponService::findByValidityAndGroupedBySellType,
                VALIDITY_GROUP, couponService::findByValidityAndGroupedByValidity,
                YEAR_GROUP, couponService::findByValidityAndGroupedByYear
        );
    }

    public List<NivoPieData> createDynamicPieData(String groupName, CouponsParameters parameters) {
        Function<CouponsParameters, List<? extends GroupingEntity<?>>> strategy = pieDataStrategies.get(groupName.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown group name: " + groupName);
        }
        List<? extends GroupingEntity<?>> result = strategy.apply(parameters);
        return NivoConvertersUtils.createPieData(result);
    }

    public <T extends IEnum> List<NivoLineData> createDynamicLineData(String upperGroup, String lowerGroup, CouponsParameters parameters) {
        GroupingFunctions<T> functions = new GroupingFunctions<>(upperGroup, lowerGroup);
        return NivoConvertersUtils.createLineData(
                couponService.findCouponEntities(parameters),
                functions.upper,
                functions.lower,
                CouponGroupingUtils::aggregateGroupSum
        );
    }

    public <T extends IEnum> List<Map<String, Object>> createDynamicBarData(String upperGroup, String lowerGroup, CouponsParameters parameters) {
        GroupingFunctions<T> functions = new GroupingFunctions<>(upperGroup, lowerGroup);
        return NivoConvertersUtils.createBarData(
                couponService.findCouponEntities(parameters),
                functions.upper,
                functions.lower
        );
    }

    public <T extends IEnum> NivoBubbleData createDynamicBubbleData(String upperGroup, String lowerGroup, CouponsParameters parameters) {
        GroupingFunctions<T> functions = new GroupingFunctions<>(upperGroup, lowerGroup);
        return NivoConvertersUtils.createBubbleData(
                couponService.findCouponEntities(parameters),
                functions.upper,
                functions.lower,
                CouponGroupingUtils::aggregateGroupSum
        );
    }

    private static class GroupingFunctions<T extends IEnum> {
        final Function<List<UpdateCouponEntity>, Map<T, List<UpdateCouponEntity>>> upper;
        final Function<List<UpdateCouponEntity>, Map<T, List<UpdateCouponEntity>>> lower;

        GroupingFunctions(String upperGroup, String lowerGroup) {
            CouponFunctionUtils.validateGroups(upperGroup, lowerGroup);
            this.upper = CouponFunctionUtils.createGrouping(upperGroup);
            this.lower = CouponFunctionUtils.createGrouping(lowerGroup);
        }
    }

}
