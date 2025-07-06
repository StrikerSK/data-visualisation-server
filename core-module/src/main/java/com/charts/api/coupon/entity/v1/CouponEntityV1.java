package com.charts.api.coupon.entity.v1;

import com.charts.general.entity.AbstractEntityV1;
import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.api.coupon.entity.enums.types.Validity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "coupon_entity_v1")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CouponEntityV1 extends AbstractEntityV1 implements Comparable<CouponEntityV1> {

	@Column(name = "platnost")
	private Validity validity;

	@Column(name = "typ_predaja")
	private SellType type;

	@Column(name = "dospeli")
	private Integer adults;

	@Column(name = "studenti")
	private Integer students;

	@Column(name = "prenosna")
	private Integer portable;

	@Column(name = "dochodcovia")
	private Integer seniors;

	@Column(name = "junior")
	private Integer junior;

	@Column(name = "dieta")
	private Integer children;

	@Override
	public int compareTo(CouponEntityV1 o) {
		int yearComparison = this.getYear().compareTo(o.getYear());
		if (yearComparison == 0) {
			return this.getMonth().getOrderValue().compareTo(o.getMonth().getOrderValue());
		} else {
			return yearComparison;
		}
	}
}
