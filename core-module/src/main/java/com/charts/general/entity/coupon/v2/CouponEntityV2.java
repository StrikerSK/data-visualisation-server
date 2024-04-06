package com.charts.general.entity.coupon.v2;

import com.charts.general.entity.AbstractEntityV2;
import com.charts.general.entity.enums.PersonType;
import com.charts.general.entity.enums.SellType;
import com.charts.general.entity.enums.Validity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "updated_coupons")
@NoArgsConstructor
@Getter
@Setter
public class CouponEntityV2 extends AbstractEntityV2 {

	@Column(name = "platnost")
	private Validity validity;

	@Column(name = "typ_predaja")
	private SellType sellType;

	@Column(name = "typ_osoby")
	private PersonType personType;

}