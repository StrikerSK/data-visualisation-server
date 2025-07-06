package com.charts.api.ticket.utils;

import com.charts.general.entity.enums.types.Months;
import com.charts.api.ticket.entity.enums.TicketType;
import com.charts.api.ticket.entity.v1.TicketEntityV1;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class TicketConverterTest extends AbstractTestNGSpringContextTests {

    protected TicketEntityV1 ticketEntityV1;

    @BeforeClass
    public void setUp() {
        ticketEntityV1 = TicketEntityV1.builder()
                .id(123L)
                .month(Months.JANUARY)
                .year(2015)
                .code("012015")
                .discounted(true)
                .fifteenMinutes(100L)
                .oneDay(200L)
                .oneDayAll(300L)
                .twoZones(400L)
                .threeZones(500L)
                .fourZones(600L)
                .fiveZones(700L)
                .sixZones(800L)
                .sevenZones(900L)
                .eightZones(1000L)
                .nineZones(1100L)
                .tenZones(1200L)
                .elevenZones(1300L)
                .build();
    }

    @Test
    public void TestTicketConverter() {
        List<UpdateTicketEntity> convertedValues = TicketConverter.convertTicketEntity(ticketEntityV1);

        Assert.assertEquals(convertedValues.size(), 13);
        Assert.assertTrue(convertedValues.stream().allMatch(e -> Objects.equals(e.getDiscounted(), ticketEntityV1.getDiscounted())));
        Assert.assertTrue(convertedValues.stream().allMatch(e -> Objects.equals(e.getYear(), ticketEntityV1.getYear())));
        Assert.assertTrue(convertedValues.stream().allMatch(e -> Objects.equals(e.getMonth(), ticketEntityV1.getMonth())));

        Stream.of(
                Pair.of(TicketType.FIFTEEN_MINUTES, ticketEntityV1.getFifteenMinutes()),
                Pair.of(TicketType.ONE_DAY, ticketEntityV1.getOneDay()),
                Pair.of(TicketType.ONE_DAY_ALL, ticketEntityV1.getOneDayAll()),
                Pair.of(TicketType.TWO_ZONES, ticketEntityV1.getTwoZones()),
                Pair.of(TicketType.THREE_ZONES, ticketEntityV1.getThreeZones()),
                Pair.of(TicketType.FOUR_ZONES, ticketEntityV1.getFourZones()),
                Pair.of(TicketType.FIVE_ZONES, ticketEntityV1.getFiveZones()),
                Pair.of(TicketType.SIX_ZONES, ticketEntityV1.getSixZones()),
                Pair.of(TicketType.SEVEN_ZONES, ticketEntityV1.getSevenZones()),
                Pair.of(TicketType.EIGHT_ZONES, ticketEntityV1.getEightZones()),
                Pair.of(TicketType.NINE_ZONES, ticketEntityV1.getNineZones()),
                Pair.of(TicketType.TEN_ZONES, ticketEntityV1.getTenZones()),
                Pair.of(TicketType.ELEVEN_ZONES, ticketEntityV1.getElevenZones())
        ).forEach(p -> findTicketEntity(convertedValues, p.getLeft(), p.getRight()));
    }

    private void findTicketEntity(List<UpdateTicketEntity> entities, TicketType ticketType, Long value) {
        Optional<UpdateTicketEntity> entity = entities.stream().filter(e -> e.getTicketType() == ticketType).findFirst();
        Assert.assertTrue(entity.isPresent(), String.format("Entity not found for %s", ticketType));
        Assert.assertEquals(entity.get().getValue(), value.intValue(), String.format("Test failed for %s", ticketType));
    }

}
