package com.charts.nivo.service;

import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.api.ticket.service.TicketService;
import com.charts.api.ticket.utils.TicketFunctionUtils;
import com.charts.general.entity.enums.IEnum;
import com.charts.nivo.utils.NivoConvertersUtils;
import com.charts.api.ticket.entity.TicketsParameters;
import com.charts.api.ticket.utils.TicketGroupingUtils;
import com.charts.nivo.entity.NivoBubbleData;
import com.charts.nivo.entity.NivoLineData;
import com.charts.nivo.entity.NivoPieData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.charts.api.ticket.utils.TicketFunctionUtils.DISCOUNTED_GROUP;
import static com.charts.api.ticket.utils.TicketFunctionUtils.TICKET_GROUP;
import static com.charts.general.utils.AbstractFunctionUtils.MONTH_GROUP;
import static com.charts.general.utils.AbstractFunctionUtils.YEAR_GROUP;

@Service
@AllArgsConstructor
public class NivoTicketsService {

	private final TicketService ticketService;

	public List<NivoPieData> createDynamicPieData(String groupName, TicketsParameters parameters) {
		List<NivoPieData> convertedData;

		switch (groupName.toLowerCase()) {
			case TICKET_GROUP:
				convertedData = NivoConvertersUtils.createPieData(ticketService.getTicketsByTicketType(parameters));
				break;
			case DISCOUNTED_GROUP:
				convertedData = NivoConvertersUtils.createPieData(ticketService.getTicketsByDiscounted(parameters));
				break;
			case MONTH_GROUP:
				convertedData = NivoConvertersUtils.createPieData(ticketService.getTicketsByMonth(parameters));
				break;
			case YEAR_GROUP:
				convertedData = NivoConvertersUtils.createPieData(ticketService.getTicketsByYear(parameters));
				break;
			default:
				throw new IllegalArgumentException("Unknown group name: " + groupName);
		}

		convertedData.sort(Comparator.comparingInt(NivoPieData::getOrderValue));
		return convertedData;
	}

	public <T extends IEnum> List<NivoLineData> createDynamicLineData(String upperGroup, String lowerGroup, TicketsParameters parameters) {
		TicketFunctionUtils.validateGroups(upperGroup, lowerGroup);
		Function<List<UpdateTicketEntity>, Map<T, List<UpdateTicketEntity>>> upperGroupingFunction = TicketFunctionUtils.createGrouping(upperGroup);
		Function<List<UpdateTicketEntity>, Map<T, List<UpdateTicketEntity>>> lowerGroupingFunction = TicketFunctionUtils.createGrouping(lowerGroup);
		return NivoConvertersUtils.createLineData(
				ticketService.getAllByFilter(parameters),
				upperGroupingFunction,
				lowerGroupingFunction,
				TicketGroupingUtils::aggregateGroupSum
		);
	}

	public <T extends IEnum> NivoBubbleData createDynamicBubbleData(String upperGroup, String lowerGroup, TicketsParameters parameters) {
		TicketFunctionUtils.validateGroups(upperGroup, lowerGroup);
		Function<List<UpdateTicketEntity>, Map<T, List<UpdateTicketEntity>>> upperGroupingFunction = TicketFunctionUtils.createGrouping(upperGroup);
		Function<List<UpdateTicketEntity>, Map<T, List<UpdateTicketEntity>>> lowerGroupingFunction = TicketFunctionUtils.createGrouping(lowerGroup);
		return NivoConvertersUtils.createBubbleData(
				ticketService.getAllByFilter(parameters),
				upperGroupingFunction,
				lowerGroupingFunction,
				TicketGroupingUtils::aggregateGroupSum
		);
	}

	public <T extends IEnum> List<Map<String, Object>> createDynamicBarData(String upperGroup, String lowerGroup, TicketsParameters parameters) {
		TicketFunctionUtils.validateGroups(upperGroup, lowerGroup);
		Function<List<UpdateTicketEntity>, Map<T, List<UpdateTicketEntity>>> upperGroupingFunction = TicketFunctionUtils.createGrouping(upperGroup);
		Function<List<UpdateTicketEntity>, Map<T, List<UpdateTicketEntity>>> lowerGroupingFunction = TicketFunctionUtils.createGrouping(lowerGroup);
		return NivoConvertersUtils.createBarData(
				ticketService.getAllByFilter(parameters),
				upperGroupingFunction,
				lowerGroupingFunction
		);
	}

}
