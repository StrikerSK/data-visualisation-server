package com.charts.api.ticket.entity.v2;

import com.charts.api.ticket.entity.enums.TicketType;
import com.charts.general.entity.AbstractUpdateEntity;
import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "tickets_entity_v2")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UpdateTicketEntity extends AbstractUpdateEntity implements Comparable<UpdateTicketEntity> {

	@Column(name = "ticket_type")
	@CsvBindByName(required = true)
	private TicketType ticketType;

	@Column(name = "discounted")
	@CsvBindByName(required = true)
	private Boolean discounted;

	@Override
	public int compareTo(UpdateTicketEntity o) {

		int yearComparison = this.getYear().compareTo(o.getYear());
		if (yearComparison == 0) {
			return this.getMonth().getOrderValue().compareTo(o.getMonth().getOrderValue());
		} else {
			return yearComparison;
		}

	}

}
