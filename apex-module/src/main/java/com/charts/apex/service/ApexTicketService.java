package com.charts.apex.service;

import com.charts.apex.entity.ApexObject;
import com.charts.general.entity.ticket.TicketsParameters;
import com.charts.general.entity.ticket.updated.UpdateTicketEntity;
import com.charts.general.entity.ticket.updated.UpdateTicketList;
import com.charts.general.repository.ticket.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ApexTicketService {

	private final TicketRepository ticketRepository;

	public List<ApexObject> getTicketTypeDataByMonth(final TicketsParameters parameters) {
		UpdateTicketList ticketList = ticketRepository.getUpdatedTicketList().filterWithParameters(parameters);
		List<ApexObject> outputMapList = new ArrayList<>();
		parameters.getTicketType().forEach(ticketType -> {
			ApexObject apexObject = new ApexObject(ticketType);
			List<Integer> values = new ArrayList<>();
			UpdateTicketList entities = ticketList.filterByTicketType(Collections.singletonList(ticketType));
			parameters.getMonths().forEach(month -> {
				Integer monthlyValue = entities.filterByMonth(Collections.singletonList(month)).getTicketEntities().stream()
						.map(UpdateTicketEntity::getValue)
						.reduce(0, Integer::sum);
				values.add(monthlyValue);
			});
			outputMapList.add(apexObject.withList(values));
		});
		return outputMapList;
	}

}
