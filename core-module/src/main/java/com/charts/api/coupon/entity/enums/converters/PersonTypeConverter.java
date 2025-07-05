package com.charts.api.coupon.entity.enums.converters;

import com.charts.api.coupon.entity.enums.types.PersonType;
import com.charts.general.entity.enums.EnumUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * To make sure that enum type is converted to database value
 */
@Converter(autoApply = true)
public class PersonTypeConverter implements AttributeConverter<PersonType, String> {

    @Override
    public String convertToDatabaseColumn(PersonType personType) {
        if (personType == null) {
            return null;
        }

        return personType.getValue();
    }

    @Override
    public PersonType convertToEntityAttribute(String personType) {
        return EnumUtils.getValue(PersonType.class, personType).orElseThrow(IllegalArgumentException::new);
    }

}
