package com.charts.api.ticket.utils;

import com.charts.api.ticket.entity.enums.TicketType;
import com.charts.api.ticket.entity.v1.TicketEntityV1;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.general.entity.enums.IEnum;
import com.charts.general.entity.enums.types.EnumAdapter;
import com.charts.general.entity.enums.types.Months;
import com.charts.general.exception.InvalidParameterException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TicketFunctionUtilsTest {

    private List<UpdateTicketEntity> ticketEntities;

    @BeforeClass
    public void setUp() {
        TicketEntityV1 ticketEntityV1 = TicketEntityV1.builder()
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

        ticketEntities = TicketConverter.convertTicketEntity(ticketEntityV1);
    }

    @Test
    public void testCreateGrouping_ticketType() {
        Map<IEnum, List<UpdateTicketEntity>> groupedValues = TicketFunctionUtils.createGrouping(TicketFunctionUtils.TICKET_GROUP).apply(ticketEntities);

        Assert.assertEquals(groupedValues.size(), TicketType.values().length);
        Assert.assertTrue(groupedValues.containsKey(TicketType.FIFTEEN_MINUTES));
        Assert.assertEquals(groupedValues.get(TicketType.FIFTEEN_MINUTES).size(), 1);
    }

    @Test
    public void testCreateGrouping_discountedUsesEnumAdapter() {
        Map<IEnum, List<UpdateTicketEntity>> groupedValues = TicketFunctionUtils.createGrouping(TicketFunctionUtils.DISCOUNTED_GROUP).apply(ticketEntities);

        Assert.assertTrue(groupedValues.keySet().stream().allMatch(EnumAdapter.class::isInstance));
        Assert.assertTrue(groupedValues.containsKey(new EnumAdapter(true)));
        Assert.assertEquals(groupedValues.get(new EnumAdapter(true)).size(), ticketEntities.size());
    }

    @Test
    public void testCreateGrouping_monthsPreservesOrderAndEmptyBuckets() {
        Map<IEnum, List<UpdateTicketEntity>> groupedValues = TicketFunctionUtils.createGrouping(TicketFunctionUtils.MONTH_GROUP).apply(ticketEntities);

        Assert.assertEquals(groupedValues.size(), Months.values().length);
        Assert.assertEquals(groupedValues.keySet().stream().findFirst().orElseThrow(), Months.JANUARY);
        Assert.assertEquals(groupedValues.get(Months.JANUARY).size(), ticketEntities.size());
        Assert.assertEquals(groupedValues.get(Months.FEBRUARY).size(), 0);
    }

    @Test
    public void testCreateGrouping_yearUsesEnumAdapter() {
        Map<IEnum, List<UpdateTicketEntity>> groupedValues = TicketFunctionUtils.createGrouping(TicketFunctionUtils.YEAR_GROUP).apply(ticketEntities);

        Assert.assertTrue(groupedValues.keySet().stream().allMatch(EnumAdapter.class::isInstance));
        Assert.assertTrue(groupedValues.containsKey(new EnumAdapter(2015)));
        Assert.assertEquals(groupedValues.get(new EnumAdapter(2015)).size(), ticketEntities.size());
    }

    @Test
    public void testCreateGrouping_invalidGroup() {
        InvalidParameterException exception = Assert.expectThrows(
                InvalidParameterException.class,
                () -> TicketFunctionUtils.createGrouping("invalid")
        );

        Assert.assertTrue(exception.getMessage().contains("Unknown group name"));
    }

    @Test
    public void testCreateGrouping_canBePassedToIEnumFunctionReference() {
        Function<List<UpdateTicketEntity>, Map<IEnum, List<UpdateTicketEntity>>> groupingFunction =
                TicketFunctionUtils.createGrouping(TicketFunctionUtils.TICKET_GROUP);

        Assert.assertEquals(groupingFunction.apply(ticketEntities).size(), TicketType.values().length);
    }
}
