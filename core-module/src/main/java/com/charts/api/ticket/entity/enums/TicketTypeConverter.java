package com.charts.api.ticket.entity.enums;

import com.charts.general.entity.enums.EnumUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TicketTypeConverter implements AttributeConverter<TicketType, String> {

    @Override
    public String convertToDatabaseColumn(TicketType ticketType) {
        if (ticketType == null) {
            return null;
        }

        return ticketType.getValue();
    }

    @Override
    public TicketType convertToEntityAttribute(String ticketType) {
        return EnumUtils.getValue(TicketType.class, ticketType).orElseThrow(IllegalArgumentException::new);
    }

}
