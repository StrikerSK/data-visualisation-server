package com.charts.api.ticket.entity;

import com.charts.api.ticket.entity.enums.TicketType;
import com.charts.general.entity.parameters.AbstractParameters;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TicketsParameters extends AbstractParameters {

	private final List<Boolean> discounted;
	private final List<String> ticketType;

	public TicketsParameters(List<String> month, List<Integer> year, List<Boolean> discounted, List<String> ticketType) {
		super(month, year);
		this.discounted = discounted;
		this.ticketType = ticketType;
	}

	public List<Boolean> getDiscounted() {
		if (CollectionUtils.isEmpty(discounted)) {
			return Stream.of(Boolean.TRUE, Boolean.FALSE).collect(Collectors.toList());
		}

		return discounted;
	}

	public List<TicketType> getTicketType() {
		return getValueList(ticketType, TicketType.class);
	}
}
