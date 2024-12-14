package com.charts.nivo.service;

import com.charts.api.coupon.service.CouponV2Service;
import com.charts.general.entity.coupon.CouponsParameters;
import com.charts.api.coupon.entity.v2.UpdateCouponList;
import com.charts.api.coupon.repository.CouponRepository;
import com.charts.general.utils.CouponGroupingUtils;
import com.charts.nivo.entity.NivoBubbleData;
import com.charts.nivo.entity.NivoDataXY;
import com.charts.nivo.entity.NivoLineData;
import com.charts.nivo.entity.NivoPieData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NivoCouponService {

    private final CouponRepository couponRepository;

    @Autowired
    private CouponV2Service couponService;

    public NivoCouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<NivoLineData> getMonthlyLineDataByPersonType(CouponsParameters parameters) {
        UpdateCouponList couponList = couponRepository.getUpdateCouponList().filterWithParameters(parameters);
        List<NivoLineData> output = new ArrayList<>();
        CouponGroupingUtils.groupByPersonType(couponList.getCouponEntityList())
                .forEach((personType, entity) -> {
                    List<NivoDataXY> nestedData = new ArrayList<>();
                    CouponGroupingUtils.groupAndSumByMonth(entity)
                            .forEach((month, integer) -> nestedData.add(new NivoDataXY(month, ((Integer) integer).longValue())));
                    output.add(new NivoLineData(personType, nestedData));
                });
        return output;
    }

    /**
     * Method gets line data by validity
     *
     * @param parameters all requested parameters
     * @return data for displaying line chart by validity
     */
    public List<NivoLineData> getMonthlyLineDataByValidity(CouponsParameters parameters) {
        UpdateCouponList couponList = couponRepository.getUpdateCouponList().filterWithParameters(parameters);
        List<NivoLineData> output = new ArrayList<>();
        CouponGroupingUtils.groupByValidity(couponList.getCouponEntityList())
                .forEach((validity, entity) -> {
                    List<NivoDataXY> nestedData = new ArrayList<>();
                    CouponGroupingUtils.groupAndSumByMonth(entity)
                            .forEach((month, integer) -> nestedData.add(new NivoDataXY(month, ((Integer) integer).longValue())));
                    output.add(new NivoLineData(validity, nestedData));
                });

        return output;
    }

    public List<NivoLineData> getMonthlyLineDataBySellType(CouponsParameters parameters) {
        UpdateCouponList couponList = couponRepository.getUpdateCouponList().filterWithParameters(parameters);
        List<NivoLineData> output = new ArrayList<>();
        CouponGroupingUtils.groupBySellType(couponList.getCouponEntityList())
                .forEach((sellType, entity) -> {
                    List<NivoDataXY> nestedData = new ArrayList<>();
                    CouponGroupingUtils.groupAndSumByMonth(entity)
                            .forEach((month, integer) -> nestedData.add(new NivoDataXY(month, ((Integer) integer).longValue())));
                    output.add(new NivoLineData(sellType, nestedData));
                });

        return output;
    }

    public List<Map<String, Object>> getMonthlyBarDataByPersonType(CouponsParameters parameters) {
        UpdateCouponList couponList = couponRepository.getUpdateCouponList().filterWithParameters(parameters);
        List<Map<String, Object>> outputMapList = new ArrayList<>();
        CouponGroupingUtils.groupByMonth(couponList.getCouponEntityList())
                .forEach((month, entities) -> {
                    Map<String, Object> tmpMap = new HashMap<>(CouponGroupingUtils.groupByAndSumByPerson(entities));
                    tmpMap.put("month", month.getValue());
                    outputMapList.add(tmpMap);
                });
        return outputMapList;
    }

    public List<Map<String, Object>> getMonthlyBarDataByValidity(CouponsParameters parameters) {
        UpdateCouponList couponList = couponRepository.getUpdateCouponList().filterWithParameters(parameters);
        List<Map<String, Object>> outputMapList = new ArrayList<>();
        CouponGroupingUtils.groupByMonth(couponList.getCouponEntityList())
                .forEach((month, entities) -> {
                    Map<String, Object> tmpMap = CouponGroupingUtils.convertMapKeysToString(CouponGroupingUtils.groupByAndSumByValidity(entities));
                    tmpMap.put("month", month.getValue());
                    outputMapList.add(tmpMap);
                });
        return outputMapList;
    }

    public List<Map<String, Object>> getMonthlyBarDataBySellType(CouponsParameters parameters) {
        UpdateCouponList couponList = couponRepository.getUpdateCouponList().filterWithParameters(parameters);
        List<Map<String, Object>> outputMapList = new ArrayList<>();
        CouponGroupingUtils.groupByMonth(couponList.getCouponEntityList())
                .forEach((month, entities) -> {
                    Map<String, Object> tmpMap = CouponGroupingUtils.convertMapKeysToString(CouponGroupingUtils.groupByAndSumBySellType(entities));
                    tmpMap.put("month", month.getValue());
                    outputMapList.add(tmpMap);
                });
        return outputMapList;
    }

    public NivoBubbleData getPersonBubbleDataByValidity(CouponsParameters parameters) {
        UpdateCouponList couponList = couponRepository.getUpdateCouponList().filterWithParameters(parameters);
        List<NivoBubbleData> middleNivoBubbleDataList = new ArrayList<>();
        CouponGroupingUtils.groupByPersonType(couponList.getCouponEntityList()).forEach((key, entity) -> {
            List<NivoBubbleData> nestedNivoBubbleDataList = new ArrayList<>();
            CouponGroupingUtils.groupByAndSumByValidity(entity)
                    .forEach((validity, integer) -> nestedNivoBubbleDataList.add(new NivoBubbleData(validity, (Integer) integer)));
            middleNivoBubbleDataList.add(new NivoBubbleData(key.getValue(), nestedNivoBubbleDataList));
        });
        return new NivoBubbleData("Predaj kupónov", middleNivoBubbleDataList);
    }

    public NivoBubbleData getPersonBubbleDataBySellType(CouponsParameters parameters) {
        UpdateCouponList couponList = couponRepository.getUpdateCouponList().filterWithParameters(parameters);
        List<NivoBubbleData> middleNivoBubbleDataList = new ArrayList<>();
        CouponGroupingUtils.groupByPersonType(couponList.getCouponEntityList()).forEach((key, entity) -> {
            List<NivoBubbleData> nestedNivoBubbleDataList = new ArrayList<>();
            CouponGroupingUtils.groupByAndSumBySellType(entity)
                    .forEach((validity, integer) -> nestedNivoBubbleDataList.add(new NivoBubbleData(validity, (Integer) integer)));
            middleNivoBubbleDataList.add(new NivoBubbleData(key.getValue(), nestedNivoBubbleDataList));
        });
        return new NivoBubbleData("Predaj kupónov", middleNivoBubbleDataList);
    }

    public List<NivoPieData> getPersonTypePieData(CouponsParameters parameters) {
        return couponService.findByValidityAndGroupedByPersoType(parameters)
                .stream()
                .map(e -> new NivoPieData(e.getKey().getValue(), e.getValue().intValue()))
                .collect(Collectors.toList());
    }

    public List<NivoPieData> getMonthlyPieData(CouponsParameters parameters) {
        return couponService.findByValidityAndGroupedByMonth(parameters)
                .stream()
                .map(e -> new NivoPieData(e.getKey().getValue(), e.getValue().intValue()))
                .collect(Collectors.toList());
    }

    public List<NivoPieData> getValidityPieData(CouponsParameters parameters) {
        return couponService.findByValidityAndGroupedByValidity(parameters)
                .stream()
                .map(e -> new NivoPieData(e.getKey().getValue(), e.getValue().intValue()))
                .collect(Collectors.toList());
    }

    public List<NivoPieData> getSellTypePieData(CouponsParameters parameters) {
        return couponService.findByValidityAndGroupedBySellType(parameters)
                .stream()
                .map(e -> new NivoPieData(e.getKey().getValue(), e.getValue().intValue()))
                .collect(Collectors.toList());
    }

}
