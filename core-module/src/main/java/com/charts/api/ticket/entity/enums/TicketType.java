package com.charts.api.ticket.entity.enums;

import com.charts.api.ticket.constants.TicketConstants;
import com.charts.general.entity.enums.IEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum TicketType implements IEnum {

    FIFTEEN_MINUTES(TicketConstants.FIFTEEN_MINUTES, "fifteen_minutes", 1),
    ONE_DAY(TicketConstants.ONE_DAY, "one_day", 2),
    ONE_DAY_ALL(TicketConstants.ONE_DAY_ALL, "one_day_all", 3),
    TWO_ZONES(TicketConstants.TWO_ZONES, "two_zones", 4),
    THREE_ZONES(TicketConstants.THREE_ZONES, "three_zones", 5),
    FOUR_ZONES(TicketConstants.FOUR_ZONES, "four_zones", 6),
    FIVE_ZONES(TicketConstants.FIVE_ZONES, "five_zones", 7),
    SIX_ZONES(TicketConstants.SIX_ZONES, "six_zones", 8),
    SEVEN_ZONES(TicketConstants.SEVEN_ZONES, "seven_zones", 9),
    EIGHT_ZONES(TicketConstants.EIGHT_ZONES, "eight_zones", 10),
    NINE_ZONES(TicketConstants.NINE_ZONES, "nine_zones", 11),
    TEN_ZONES(TicketConstants.TEN_ZONES, "ten_zones", 12),
    ELEVEN_ZONES(TicketConstants.ELEVEN_ZONES, "eleven_zones", 13);

    private final String value;
    private final String systemValue;
    private final Integer orderValue;

    TicketType(String value, String systemValue, Integer orderValue) {
        this.value = value;
        this.systemValue = systemValue;
        this.orderValue = orderValue;
    }

    public static Optional<TicketType> getType(String label) {
        return Arrays.stream(TicketType.values())
                .filter(c -> c.getValue().equals(label))
                .findFirst();
    }

}
