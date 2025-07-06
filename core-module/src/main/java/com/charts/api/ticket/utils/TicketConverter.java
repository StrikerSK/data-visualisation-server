package com.charts.api.ticket.utils;

import com.charts.api.ticket.entity.enums.TicketType;
import com.charts.api.ticket.entity.v1.TicketEntityV1;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.charts.api.ticket.constants.TicketConstants.*;

public class TicketConverter {

    public static List<UpdateTicketEntity> convertTicketEntity(TicketEntityV1 ticketEntities) {
        return fillData(ticketEntities);
    }

    public static List<UpdateTicketEntity> convertTicketEntity(List<TicketEntityV1> ticketEntities) {
        return ticketEntities.stream()
                .map(TicketConverter::fillData)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<UpdateTicketEntity> fillData(TicketEntityV1 ticketEntityV1) {
        List<UpdateTicketEntity> ticketEntities = new ArrayList<>();
        ticketEntities.add(extractData(FIFTEEN_MINUTES, ticketEntityV1, TicketEntityV1::getFifteenMinutes));
        ticketEntities.add(extractData(ONE_DAY, ticketEntityV1, TicketEntityV1::getOneDay));
        ticketEntities.add(extractData(ONE_DAY_ALL, ticketEntityV1, TicketEntityV1::getOneDayAll));
        ticketEntities.add(extractData(TWO_ZONES, ticketEntityV1, TicketEntityV1::getTwoZones));
        ticketEntities.add(extractData(THREE_ZONES, ticketEntityV1, TicketEntityV1::getThreeZones));
        ticketEntities.add(extractData(FOUR_ZONES, ticketEntityV1, TicketEntityV1::getFourZones));
        ticketEntities.add(extractData(FIVE_ZONES, ticketEntityV1, TicketEntityV1::getFiveZones));
        ticketEntities.add(extractData(SIX_ZONES, ticketEntityV1, TicketEntityV1::getSixZones));
        ticketEntities.add(extractData(SEVEN_ZONES, ticketEntityV1, TicketEntityV1::getSevenZones));
        ticketEntities.add(extractData(EIGHT_ZONES, ticketEntityV1, TicketEntityV1::getEightZones));
        ticketEntities.add(extractData(NINE_ZONES, ticketEntityV1, TicketEntityV1::getNineZones));
        ticketEntities.add(extractData(TEN_ZONES, ticketEntityV1, TicketEntityV1::getTenZones));
        ticketEntities.add(extractData(ELEVEN_ZONES, ticketEntityV1, TicketEntityV1::getElevenZones));
        return ticketEntities;
    }

    private static UpdateTicketEntity extractData(String ticketType, TicketEntityV1 ticketEntityV1, Function<TicketEntityV1, Long> function) {
        UpdateTicketEntity output = new UpdateTicketEntity();

        //From UpdateCouponEntity class
        output.setValue(function.apply(ticketEntityV1).intValue());
        output.setDiscounted(ticketEntityV1.getDiscounted());
        output.setTicketType(TicketType.getType(ticketType).get());

        // From GeneralEntity class
        output.setMonth(ticketEntityV1.getMonth());
        output.setYear(ticketEntityV1.getYear());

        return output;
    }

}
