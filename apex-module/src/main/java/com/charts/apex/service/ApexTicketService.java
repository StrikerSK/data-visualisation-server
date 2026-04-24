package com.charts.apex.service;

import com.charts.apex.entity.ApexObject;
import com.charts.api.ticket.entity.TicketsParameters;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.api.ticket.service.TicketService;
import com.charts.api.ticket.utils.TicketFunctionUtils;
import com.charts.general.entity.enums.IEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class ApexTicketService extends ApexAbstractService {

	private final TicketService ticketService;

	public List<ApexObject> getTicketData(
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

}
