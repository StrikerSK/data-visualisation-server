package com.charts.api.coupon.entity.enums.converters;

import com.charts.api.coupon.entity.enums.types.SellType;
import com.charts.general.entity.enums.EnumUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SellTypeConverter implements AttributeConverter<SellType, String> {

    @Override
    public String convertToDatabaseColumn(SellType sellType) {
        if (sellType == null) {
            return null;
        }

        return sellType.getValue();
    }

    @Override
    public SellType convertToEntityAttribute(String sellType) {
        return EnumUtils.getValue(SellType.class, sellType).orElseThrow(IllegalArgumentException::new);
    }

}
