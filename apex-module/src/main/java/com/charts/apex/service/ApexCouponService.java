package com.charts.apex.service;

import com.charts.apex.entity.ApexObject;
import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.coupon.utils.CouponFunctionUtils;
import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.general.entity.enums.IEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class ApexCouponService extends ApexAbstractService {

	private final CouponV2Service couponService;

	public List<ApexObject> getCouponData(
			String upperGroup,
			String lowerGroup,
			CouponsParameters parameters
	) {
		CouponFunctionUtils.validateGroups(upperGroup, lowerGroup);

		Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> upperFunction = CouponFunctionUtils.createGrouping(upperGroup);
		Function<List<UpdateCouponEntity>, Map<IEnum, List<UpdateCouponEntity>>> lowerFunction = CouponFunctionUtils.createGrouping(lowerGroup);

		List<UpdateCouponEntity> couponList = couponService.findCouponEntities(parameters);
		return processValues(couponList, upperFunction, lowerFunction);
	}

}
