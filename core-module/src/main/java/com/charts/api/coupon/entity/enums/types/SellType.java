package com.charts.api.coupon.entity.enums.types;

import com.charts.general.entity.enums.IEnum;
import lombok.Getter;

import static com.charts.api.coupon.constants.EnumerationCouponConstants.CHIP_CARD;
import static com.charts.api.coupon.constants.EnumerationCouponConstants.E_SHOP;
import static com.charts.api.coupon.constants.EnumerationCouponConstants.PAPER_COUPON;

@Getter
public enum SellType implements IEnum {

    CARD(CHIP_CARD, "chip_card", 1),
    COUPON(PAPER_COUPON, "paper_coupon", 2),
    ESHOP(E_SHOP, "e_shop", 3);

    private final String value;
    private final String systemValue;
    private final Integer orderValue;

    SellType(String value, String systemValue, Integer orderValue) {
        this.value = value;
        this.systemValue = systemValue;
        this.orderValue = orderValue;
    }

}
