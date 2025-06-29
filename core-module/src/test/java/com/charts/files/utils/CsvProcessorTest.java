package com.charts.files.utils;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.enums.types.PersonType;
import com.charts.api.coupon.enums.types.SellType;
import com.charts.api.coupon.enums.types.Validity;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.general.entity.AbstractUpdateEntity;
import com.charts.general.entity.enums.types.Months;
import com.charts.files.exception.CsvContentException;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class CsvProcessorTest {

    @Test(expectedExceptions = CsvContentException.class, expectedExceptionsMessageRegExp = "Header is missing required fields \\[DISCOUNTED, TICKETTYPE]. The list of headers encountered is \\[MONTH,PERSONTYPE,SELLTYPE,VALIDITY,VALUE,YEAR].")
    public void testFileRead_Wrong() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("CouponData.csv");
        CsvProcessor.readEntries(inputStream, UpdateTicketEntity.class);
    }

    @Test
    public void testFileRead() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("CouponData.csv");
        List<UpdateCouponEntity> couponList = CsvProcessor.readEntries(inputStream, UpdateCouponEntity.class);

        Assert.assertEquals(couponList.size(), 6);

        Assert.assertEquals(countValues(couponList, Validity.MONTHLY, UpdateCouponEntity::getValidity), 3);
        Assert.assertEquals(sumValues(couponList, Validity.MONTHLY, UpdateCouponEntity::getValidity), 600);

        Assert.assertEquals(countValues(couponList, Validity.THREE_MONTHS, UpdateCouponEntity::getValidity), 3);
        Assert.assertEquals(sumValues(couponList, Validity.THREE_MONTHS, UpdateCouponEntity::getValidity), 6000);

        Assert.assertEquals(countValues(couponList, SellType.CARD, UpdateCouponEntity::getSellType), 3);
        Assert.assertEquals(sumValues(couponList, SellType.CARD, UpdateCouponEntity::getSellType), 600);

        Assert.assertEquals(countValues(couponList, SellType.ESHOP, UpdateCouponEntity::getSellType), 3);
        Assert.assertEquals(sumValues(couponList, SellType.ESHOP, UpdateCouponEntity::getSellType), 6000);

        Assert.assertEquals(countValues(couponList, PersonType.ADULT, UpdateCouponEntity::getPersonType), 3);
        Assert.assertEquals(sumValues(couponList, PersonType.ADULT, UpdateCouponEntity::getPersonType), 600);

        Assert.assertEquals(countValues(couponList, PersonType.STUDENT, UpdateCouponEntity::getPersonType), 3);
        Assert.assertEquals(sumValues(couponList, PersonType.STUDENT, UpdateCouponEntity::getPersonType), 6000);

        Assert.assertEquals(countValues(couponList, Months.JANUARY, UpdateCouponEntity::getMonth), 2);
        Assert.assertEquals(sumValues(couponList, Months.JANUARY, UpdateCouponEntity::getMonth), 1100);

        Assert.assertEquals(countValues(couponList, Months.FEBRUARY, UpdateCouponEntity::getMonth), 2);
        Assert.assertEquals(sumValues(couponList, Months.FEBRUARY, UpdateCouponEntity::getMonth), 2200);

        Assert.assertEquals(countValues(couponList, Months.MARCH, UpdateCouponEntity::getMonth), 2);
        Assert.assertEquals(sumValues(couponList, Months.MARCH, UpdateCouponEntity::getMonth), 3300);

        Assert.assertEquals(countValues(couponList, 2024, UpdateCouponEntity::getYear), 4);
        Assert.assertEquals(sumValues(couponList, 2024, UpdateCouponEntity::getYear), 3600);

        Assert.assertEquals(countValues(couponList, 2023, UpdateCouponEntity::getYear), 2);
        Assert.assertEquals(sumValues(couponList, 2023, UpdateCouponEntity::getYear), 3000);
    }

    @Test
    public void testFileWrite() throws IOException {
        Writer writer = new StringWriter();
        UpdateCouponEntity entity1 = UpdateCouponEntity.builder()
                .year(2024)
                .month(Months.JANUARY)
                .personType(PersonType.ADULT)
                .validity(Validity.MONTHLY)
                .sellType(SellType.CARD)
                .value(100)
                .build();

        UpdateCouponEntity entity2 = UpdateCouponEntity.builder()
                .year(2024)
                .month(Months.FEBRUARY)
                .personType(PersonType.ADULT)
                .validity(Validity.MONTHLY)
                .sellType(SellType.CARD)
                .value(200)
                .build();

        UpdateCouponEntity entity3 = UpdateCouponEntity.builder()
                .year(2023)
                .month(Months.MARCH)
                .personType(PersonType.ADULT)
                .validity(Validity.YEARLY)
                .sellType(SellType.ESHOP)
                .value(300)
                .build();

        CsvProcessor.writeEntries(writer, List.of(entity1, entity2, entity3));
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ResultCouponData.csv");
        assert inputStream != null;
        String expString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        Assert.assertEquals(writer.toString().trim(), expString.trim());
    }

    private <T> Long countValues(List<UpdateCouponEntity> inputList, T filterType, Function<UpdateCouponEntity, T> function) {
        return getEntriesList(inputList, filterType, function).count();
    }

    private <T> Integer sumValues(List<UpdateCouponEntity> inputList, T filterType, Function<UpdateCouponEntity, T> function) {
        return getEntriesList(inputList, filterType, function).reduce(0, Integer::sum);
    }

    private <T> Stream<Integer> getEntriesList(List<UpdateCouponEntity> inputList, T filterType, Function<UpdateCouponEntity, T> function) {
        return inputList.stream().filter(e -> function.apply(e).equals(filterType)).map(AbstractUpdateEntity::getValue);
    }

}
