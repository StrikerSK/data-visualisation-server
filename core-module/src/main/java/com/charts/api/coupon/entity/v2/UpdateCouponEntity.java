package com.charts.api.coupon.entity.v2;

import com.charts.api.coupon.entity.enums.types.PersonType;
import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.api.coupon.entity.enums.types.Validity;
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
@Table(name = "coupon_entity_v2")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UpdateCouponEntity extends AbstractUpdateEntity implements Comparable<UpdateCouponEntity> {

	public static final String PERSON_TYPE_COLUMN = "person_type";
	public static final String SELL_TYPE_COLUMN = "sell_type";
	public static final String VALIDITY_COLUMN = "validity";

	@Column(name = VALIDITY_COLUMN)
	@CsvBindByName(required = true)
	private Validity validity;

	@Column(name = SELL_TYPE_COLUMN)
	@CsvBindByName(required = true)
	private SellType sellType;

	@Column(name = PERSON_TYPE_COLUMN)
	@CsvBindByName(required = true)
	private PersonType personType;

	@Override
	public int compareTo(UpdateCouponEntity o) {
		int yearComparison = this.getYear().compareTo(o.getYear());
		if (yearComparison == 0) {
			return this.getMonth().getOrderValue().compareTo(o.getMonth().getOrderValue());
		} else {
			return yearComparison;
		}
	}

}
