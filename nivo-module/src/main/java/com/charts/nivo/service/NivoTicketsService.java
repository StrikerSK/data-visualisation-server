package com.charts.nivo.service;

import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.api.ticket.service.TicketService;
import com.charts.api.ticket.utils.TicketFunctionUtils;
import com.charts.general.entity.GroupingEntity;
import com.charts.general.entity.enums.IEnum;
import com.charts.nivo.utils.NivoConvertersUtils;
import com.charts.api.ticket.entity.TicketsParameters;
import com.charts.api.ticket.utils.TicketGroupingUtils;
import com.charts.nivo.entity.NivoBubbleData;
import com.charts.nivo.entity.NivoLineData;
import com.charts.nivo.entity.NivoPieData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.charts.api.ticket.utils.TicketFunctionUtils.DISCOUNTED_GROUP;
import static com.charts.api.ticket.utils.TicketFunctionUtils.TICKET_GROUP;
import static com.charts.general.utils.AbstractFunctionUtils.MONTH_GROUP;
import static com.charts.general.utils.AbstractFunctionUtils.YEAR_GROUP;
import static com.charts.general.utils.AbstractFunctionUtils.validateGroups;

@Service
public class NivoTicketsService {

	private final TicketService ticketService;
	private final Map<String, Function<TicketsParameters, List<? extends GroupingEntity<?>>>> pieDataStrategies;

	public NivoTicketsService(TicketService ticketService) {
		this.ticketService = ticketService;
		this.pieDataStrategies = Map.of(
				TICKET_GROUP, ticketService::getTicketsByTicketType,
				DISCOUNTED_GROUP, ticketService::getTicketsByDiscounted,
				MONTH_GROUP, ticketService::getTicketsByMonth,
				YEAR_GROUP, ticketService::getTicketsByYear
		);
	}

	public List<NivoPieData> createDynamicPieData(String groupName, TicketsParameters parameters) {
		Function<TicketsParameters, List<? extends GroupingEntity<?>>> strategy = pieDataStrategies.get(groupName.toLowerCase());
		if (strategy == null) {
			throw new IllegalArgumentException("Unknown group name: " + groupName);
		}
		List<? extends GroupingEntity<?>> result = strategy.apply(parameters);
		return NivoConvertersUtils.createPieData(result);
	}

	public <T extends IEnum> List<NivoLineData> createDynamicLineData(String upperGroup, String lowerGroup, TicketsParameters parameters) {
		GroupingFunctions<T> functions = new GroupingFunctions<>(upperGroup, lowerGroup);
		return NivoConvertersUtils.createLineData(
				ticketService.getAllByFilter(parameters),
				functions.upper,
				functions.lower,
				TicketGroupingUtils::aggregateGroupSum
		);
	}

	public <T extends IEnum> NivoBubbleData createDynamicBubbleData(String upperGroup, String lowerGroup, TicketsParameters parameters) {
		GroupingFunctions<T> functions = new GroupingFunctions<>(upperGroup, lowerGroup);
		return NivoConvertersUtils.createBubbleData(
				ticketService.getAllByFilter(parameters),
				functions.upper,
				functions.lower,
				TicketGroupingUtils::aggregateGroupSum
		);
	}

	public <T extends IEnum> List<Map<String, Object>> createDynamicBarData(String upperGroup, String lowerGroup, TicketsParameters parameters) {
		GroupingFunctions<T> functions = new GroupingFunctions<>(upperGroup, lowerGroup);
		return NivoConvertersUtils.createBarData(
				ticketService.getAllByFilter(parameters),
				functions.upper,
				functions.lower
		);
	}

	private static class GroupingFunctions<T extends IEnum> {
		final Function<List<UpdateTicketEntity>, Map<T, List<UpdateTicketEntity>>> upper;
		final Function<List<UpdateTicketEntity>, Map<T, List<UpdateTicketEntity>>> lower;

		GroupingFunctions(String upperGroup, String lowerGroup) {
			validateGroups(upperGroup, lowerGroup);
			this.upper = TicketFunctionUtils.createGrouping(upperGroup);
			this.lower = TicketFunctionUtils.createGrouping(lowerGroup);
		}
	}

}
