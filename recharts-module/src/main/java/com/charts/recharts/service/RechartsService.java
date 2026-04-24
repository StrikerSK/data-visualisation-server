package com.charts.recharts.service;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.coupon.utils.CouponFunctionUtils;
import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.api.ticket.entity.TicketsParameters;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.api.ticket.service.TicketService;
import com.charts.api.ticket.utils.TicketFunctionUtils;
import com.charts.general.entity.AbstractUpdateEntity;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.utils.AbstractGroupingUtils;
import com.charts.recharts.entity.RechartsDataObject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RechartsService {

	private final CouponV2Service couponService;
	private final TicketService ticketService;

	public List<List<RechartsDataObject>> getCouponData(
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

	public List<List<RechartsDataObject>> getTicketData(
			String upperGroup,
			String lowerGroup,
			TicketsParameters parameters
	) {
		TicketFunctionUtils.validateGroups(upperGroup, lowerGroup);

		Function<List<UpdateTicketEntity>, Map<IEnum, List<UpdateTicketEntity>>> upperFunction = TicketFunctionUtils.createGrouping(upperGroup);
		Function<List<UpdateTicketEntity>, Map<IEnum, List<UpdateTicketEntity>>> lowerFunction = TicketFunctionUtils.createGrouping(lowerGroup);

		List<UpdateTicketEntity> couponList = ticketService.getAllByFilter(parameters);
		return processValues(couponList, upperFunction, lowerFunction);
	}

	/**
	 * Method creates data structure according the Recharts model
	 *
	 * @param entries List of entries that will be grouped
	 * @param upperFunction Name of the group that will be on upper level
	 * @param lowerFunction Name of the group that will be on lower level
	 * @return List of data that are grouped by specified group levels
	 * @param <R> Type that should be utilizing {@link AbstractUpdateEntity} because of value
	 */
	private static <R extends AbstractUpdateEntity> List<List<RechartsDataObject>> processValues(
			List<R> entries,
			Function<List<R>, Map<IEnum, List<R>>> upperFunction,
			Function<List<R>, Map<IEnum, List<R>>> lowerFunction
	) {
		return upperFunction.apply(entries)
				.entrySet()
				.stream()
				.map(upper -> AbstractGroupingUtils.aggregateGroupsSum(upper.getValue(), lowerFunction)
                        .entrySet()
                        .stream()
                        .map(lower -> new RechartsDataObject(upper.getKey(), lower.getKey(), ((Long) lower.getValue()).intValue()))
                        .collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

}
