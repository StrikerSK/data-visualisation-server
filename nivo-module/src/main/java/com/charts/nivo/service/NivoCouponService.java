package com.charts.nivo.service;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.coupon.utils.CouponFunctionUtils;
import com.charts.api.coupon.utils.CouponGroupingUtils;
import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.general.entity.enums.IEnum;
import com.charts.nivo.utils.NivoConvertersUtils;
import com.charts.nivo.entity.NivoBubbleData;
import com.charts.nivo.entity.NivoLineData;
import com.charts.nivo.entity.NivoPieData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.Comparator;

import static com.charts.api.coupon.utils.CouponFunctionUtils.MONTH_GROUP;
import static com.charts.api.coupon.utils.CouponFunctionUtils.PERSON_GROUP;
import static com.charts.api.coupon.utils.CouponFunctionUtils.SELL_GROUP;
import static com.charts.api.coupon.utils.CouponFunctionUtils.VALIDITY_GROUP;
import static com.charts.api.coupon.utils.CouponFunctionUtils.YEAR_GROUP;

@Service
@AllArgsConstructor
public class NivoCouponService {

    private final CouponV2Service couponService;

    public List<NivoPieData> createDynamicPieData(String groupName, CouponsParameters parameters) {
        List<NivoPieData> convertedData = switch (groupName.toLowerCase()) {
            case PERSON_GROUP ->
                    NivoConvertersUtils.createPieData(couponService.findByValidityAndGroupedByPersonType(parameters));
            case MONTH_GROUP ->
                    NivoConvertersUtils.createPieData(couponService.findByValidityAndGroupedByMonth(parameters));
            case SELL_GROUP ->
                    NivoConvertersUtils.createPieData(couponService.findByValidityAndGroupedBySellType(parameters));
            case VALIDITY_GROUP ->
                    NivoConvertersUtils.createPieData(couponService.findByValidityAndGroupedByValidity(parameters));
            case YEAR_GROUP ->
                    NivoConvertersUtils.createPieData(couponService.findByValidityAndGroupedByYear(parameters));
            default -> throw new IllegalArgumentException("Unknown group name: " + groupName);
        };

        convertedData.sort(Comparator.comparingInt(NivoPieData::getOrderValue));
        return convertedData;
    }

    public <T extends IEnum> List<NivoLineData> createDynamicLineData(String upperGroup, String lowerGroup, CouponsParameters parameters) {
       CouponFunctionUtils.validateGroups(upperGroup, lowerGroup);
        Function<List<UpdateCouponEntity>, Map<T, List<UpdateCouponEntity>>> upperGroupingFunction = CouponFunctionUtils.createGrouping(upperGroup);
        Function<List<UpdateCouponEntity>, Map<T, List<UpdateCouponEntity>>> lowerGroupingFunction = CouponFunctionUtils.createGrouping(lowerGroup);
        return NivoConvertersUtils.createLineData(
                couponService.findCouponEntities(parameters),
                upperGroupingFunction,
                lowerGroupingFunction,
                CouponGroupingUtils::aggregateGroupSum
        );
    }

    public <T extends IEnum> List<Map<String, Object>> createDynamicBarData(String upperGroup, String lowerGroup, CouponsParameters parameters) {
        CouponFunctionUtils.validateGroups(upperGroup, lowerGroup);
        Function<List<UpdateCouponEntity>, Map<T, List<UpdateCouponEntity>>> upperGroupingFunction = CouponFunctionUtils.createGrouping(upperGroup);
        Function<List<UpdateCouponEntity>, Map<T, List<UpdateCouponEntity>>> lowerGroupingFunction = CouponFunctionUtils.createGrouping(lowerGroup);
        return NivoConvertersUtils.createBarData(
                couponService.findCouponEntities(parameters),
                upperGroupingFunction,
                lowerGroupingFunction
        );
    }

    public <T extends IEnum> NivoBubbleData createDynamicBubbleData(String upperGroup, String lowerGroup, CouponsParameters parameters) {
        CouponFunctionUtils.validateGroups(upperGroup, lowerGroup);
        Function<List<UpdateCouponEntity>, Map<T, List<UpdateCouponEntity>>> upperGroupingFunction = CouponFunctionUtils.createGrouping(upperGroup);
        Function<List<UpdateCouponEntity>, Map<T, List<UpdateCouponEntity>>> lowerGroupingFunction = CouponFunctionUtils.createGrouping(lowerGroup);
        return NivoConvertersUtils.createBubbleData(
                couponService.findCouponEntities(parameters),
                upperGroupingFunction,
                lowerGroupingFunction,
                CouponGroupingUtils::aggregateGroupSum
        );
    }

}
