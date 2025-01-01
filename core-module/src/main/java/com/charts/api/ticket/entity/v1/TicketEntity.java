package com.charts.api.ticket.entity.v1;

import com.charts.general.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pid_jizdenky")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TicketEntity extends AbstractEntity implements Comparable<TicketEntity> {

	@Column(name = "zlavneny")
	private Boolean discounted;

	@Column(name = "stvrt_minut")
	private Long fifteenMinutes;

	@Column(name = "jeden_den")
	private Long oneDay;

	@Column(name = "jeden_den_vsetky")
	private Long oneDayAll;

	@Column(name = "dve_pasma")
	private Long twoZones;

	@Column(name = "tri_pasma")
	private Long threeZones;

	@Column(name = "styri_pasma")
	private Long fourZones;

	@Column(name = "pat_pasem")
	private Long fiveZones;

	@Column(name = "sest_pasem")
	private Long sixZones;

	@Column(name = "sedem_pasem")
	private Long sevenZones;

	@Column(name = "osem_pasem")
	private Long eightZones;

	@Column(name = "devat_pasem")
	private Long nineZones;

	@Column(name = "desat_pasem")
	private Long tenZones;

	@Column(name = "jedenast_pasem")
	private Long elevenZones;

	@Override
	public int compareTo(TicketEntity o) {
		int yearComparison = this.getYear().compareTo(o.getYear());
		if (yearComparison == 0) {
			return this.getMonth().getOrderValue().compareTo(o.getMonth().getOrderValue());
		} else {
			return yearComparison;
		}
	}
}
