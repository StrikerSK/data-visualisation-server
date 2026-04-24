package com.charts.general.entity.parameters;

import com.charts.api.coupon.entity.CouponsParameters;
import com.charts.api.ticket.entity.TicketsParameters;
import com.charts.general.exception.InvalidParameterException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class ParametersValidationTest {

    @Test
    public void testCouponParametersRejectUnknownEnumValue() {
        CouponsParameters parameters = new CouponsParameters(
                List.of("not-valid"),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        InvalidParameterException exception = Assert.expectThrows(InvalidParameterException.class, parameters::getValidity);
        Assert.assertTrue(exception.getMessage().contains("Unknown values [not-valid]"));
    }

    @Test
    public void testTicketParametersRejectUnknownEnumValue() {
        TicketsParameters parameters = new TicketsParameters(
                List.of(),
                List.of(),
                List.of(),
                List.of("invalid-ticket-type")
        );

        InvalidParameterException exception = Assert.expectThrows(InvalidParameterException.class, parameters::getTicketType);
        Assert.assertTrue(exception.getMessage().contains("Unknown values [invalid-ticket-type]"));
    }
}
