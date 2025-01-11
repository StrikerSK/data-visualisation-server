package com.charts.api.coupon.enums.converters;

import com.charts.general.entity.enums.EnumUtils;
import com.charts.api.coupon.enums.types.PersonType;
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
