package com.charts.nivo.service;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.coupon.utils.CouponFunctionUtils;
import com.charts.api.coupon.utils.CouponGroupingUtils;
import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.general.entity.enums.IEnum;
import com.charts.nivo.Utils.NivoConvertersUtils;
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
        List<NivoPieData> convertedData;

        switch (groupName.toLowerCase()) {
            case PERSON_GROUP:
                convertedData = NivoConvertersUtils.createPieData(couponService.findByValidityAndGroupedByPersonType(parameters));
                break;
            case MONTH_GROUP:
                convertedData = NivoConvertersUtils.createPieData(couponService.findByValidityAndGroupedByMonth(parameters));
                break;
            case SELL_GROUP:
                convertedData = NivoConvertersUtils.createPieData(couponService.findByValidityAndGroupedBySellType(parameters));
                break;
            case VALIDITY_GROUP:
                convertedData = NivoConvertersUtils.createPieData(couponService.findByValidityAndGroupedByValidity(parameters));
                break;
            case YEAR_GROUP:
                convertedData = NivoConvertersUtils.createPieData(couponService.findByValidityAndGroupedByYear(parameters));
                break;
            default:
                throw new IllegalArgumentException("Unknown group name: " + groupName);
        }

        convertedData.sort(Comparator.comparingInt(NivoPieData::getOrderValue));
        return convertedData;
    }

    public List<NivoLineData> createDynamicLineData(String upperGroup, String lowerGroup, CouponsParameters parameters) {
       CouponFunctionUtils.validateGroups(upperGroup, lowerGroup);
        Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> upperGroupingFunction = CouponFunctionUtils.createGrouping(upperGroup);
        Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> lowerGroupingFunction = CouponFunctionUtils.createGrouping(lowerGroup);
        return NivoConvertersUtils.createLineData(
                couponService.findCouponEntities(parameters),
                upperGroupingFunction,
                lowerGroupingFunction,
                CouponGroupingUtils::aggregateGroupSum
        );
    }

    public List<Map<String, Object>> createDynamicBarData(String upperGroup, String lowerGroup, CouponsParameters parameters) {
        CouponFunctionUtils.validateGroups(upperGroup, lowerGroup);
        Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> upperGroupingFunction = CouponFunctionUtils.createGrouping(upperGroup);
        Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> lowerGroupingFunction = CouponFunctionUtils.createGrouping(lowerGroup);
        return NivoConvertersUtils.createBarData(
                couponService.findCouponEntities(parameters),
                upperGroupingFunction,
                lowerGroupingFunction
        );
    }

    public NivoBubbleData createDynamicBubbleData(String upperGroup, String lowerGroup, CouponsParameters parameters) {
        CouponFunctionUtils.validateGroups(upperGroup, lowerGroup);
        Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> upperGroupingFunction = CouponFunctionUtils.createGrouping(upperGroup);
        Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> lowerGroupingFunction = CouponFunctionUtils.createGrouping(lowerGroup);
        return NivoConvertersUtils.createBubbleData(
                couponService.findCouponEntities(parameters),
                upperGroupingFunction,
                lowerGroupingFunction,
                CouponGroupingUtils::aggregateGroupSum
        );
    }

}
