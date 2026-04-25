package com.charts.files.service;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.api.ticket.service.TicketService;
import com.charts.files.exception.CsvContentException;
import com.charts.files.exception.FileProcessingException;
import com.charts.files.generator.IDataGenerator;
import com.charts.files.utils.CsvFileHandler;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class FileServiceTest {

    @Mock
    private CouponV2Service couponService;

    @Mock
    private TicketService ticketService;

    @Mock
    private IDataGenerator dataGenerator;

    @Mock
    private CsvFileHandler csvFileHandler;

    private AutoCloseable closeable;
    private FileService fileService;

    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        fileService = new FileService(couponService, ticketService, dataGenerator, csvFileHandler);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testFetchCouponsUsesGeneratorWhenRandomRequested() {
        List<UpdateCouponEntity> generatedCoupons = List.of(UpdateCouponEntity.builder().build());
        when(dataGenerator.generateCoupons(5)).thenReturn(generatedCoupons);

        List<UpdateCouponEntity> result = fileService.fetchCoupons(5, true);

        Assert.assertSame(result, generatedCoupons);
        verify(dataGenerator).generateCoupons(5);
        verifyNoInteractions(couponService);
    }

    @Test
    public void testFetchCouponsUsesPersistenceWhenRandomFlagMissing() {
        List<UpdateCouponEntity> storedCoupons = List.of(UpdateCouponEntity.builder().build());
        when(couponService.findAll(7)).thenReturn(storedCoupons);

        List<UpdateCouponEntity> result = fileService.fetchCoupons(7, null);

        Assert.assertSame(result, storedCoupons);
        verify(couponService).findAll(7);
        verifyNoInteractions(dataGenerator);
    }

    @Test
    public void testFetchTicketsUsesGeneratorWhenRandomRequested() {
        List<UpdateTicketEntity> generatedTickets = List.of(UpdateTicketEntity.builder().build());
        when(dataGenerator.generateTickets(3)).thenReturn(generatedTickets);

        List<UpdateTicketEntity> result = fileService.fetchTickets(3, true);

        Assert.assertSame(result, generatedTickets);
        verify(dataGenerator).generateTickets(3);
        verifyNoInteractions(ticketService);
    }

    @Test
    public void testFetchTicketsUsesPersistenceWhenRandomFalse() {
        List<UpdateTicketEntity> storedTickets = List.of(UpdateTicketEntity.builder().build());
        when(ticketService.findAll(9)).thenReturn(storedTickets);

        List<UpdateTicketEntity> result = fileService.fetchTickets(9, false);

        Assert.assertSame(result, storedTickets);
        verify(ticketService).findAll(9);
        verifyNoInteractions(dataGenerator);
    }

    @Test
    public void testProcessCouponsPersistsParsedEntries() throws Exception {
        MockMultipartFile payload = new MockMultipartFile(
                "payload",
                "CouponData.csv",
                "text/csv",
                """
                MONTH,PERSONTYPE,SELLTYPE,VALIDITY,VALUE,YEAR
                JANUARY,ADULT,CARD,MONTHLY,100,2024
                """.getBytes(StandardCharsets.UTF_8)
        );
        List<UpdateCouponEntity> entries = List.of(UpdateCouponEntity.builder().build());

        doNothing().when(couponService).saveAll(anyList());
        when(csvFileHandler.readEntries(any(InputStream.class), eq(UpdateCouponEntity.class))).thenReturn(entries);

        fileService.processCoupons(payload);

        verify(couponService).saveAll(entries);
    }

    @Test
    public void testProcessCouponsRejectsMalformedCsv() throws Exception {
        MockMultipartFile payload = new MockMultipartFile(
                "payload",
                "CouponData.csv",
                "text/csv",
                """
                MONTH,SELLTYPE,VALIDITY,VALUE,YEAR
                January,chip card,monthly,100,2024
                """.getBytes(StandardCharsets.UTF_8)
        );
        when(csvFileHandler.readEntries(any(InputStream.class), eq(UpdateCouponEntity.class)))
                .thenThrow(new CsvContentException("Header is missing required fields"));

        CsvContentException exception = Assert.expectThrows(CsvContentException.class, () -> fileService.processCoupons(payload));
        Assert.assertTrue(exception.getMessage().contains("Header is missing required fields"));
    }

    @Test
    public void testProcessCouponsWrapsUnexpectedInputStreamFailure() throws IOException {
        MultipartFile payload = mock(MultipartFile.class);
        when(payload.getInputStream()).thenThrow(new IOException("boom"));

        FileProcessingException exception = Assert.expectThrows(FileProcessingException.class, () -> fileService.processCoupons(payload));
        Assert.assertEquals(exception.getMessage(), "Failed to process uploaded CSV file");
    }

    @Test
    public void testProcessTicketsPersistsParsedEntries() throws Exception {
        MockMultipartFile payload = new MockMultipartFile(
                "payload",
                "TicketData.csv",
                "text/csv",
                """
                MONTH,TICKETTYPE,DISCOUNTED,VALUE,YEAR
                JANUARY,SINGLE,true,30,2024
                """.getBytes(StandardCharsets.UTF_8)
        );
        List<UpdateTicketEntity> entries = List.of(UpdateTicketEntity.builder().build());

        doNothing().when(ticketService).saveAll(anyList());
        when(csvFileHandler.readEntries(any(InputStream.class), eq(UpdateTicketEntity.class))).thenReturn(entries);

        fileService.processTickets(payload);

        verify(ticketService).saveAll(entries);
    }

    @Test
    public void testProcessTicketsRejectsMalformedCsv() throws Exception {
        MockMultipartFile payload = new MockMultipartFile(
                "payload",
                "TicketData.csv",
                "text/csv",
                """
                MONTH,VALUE,YEAR
                JANUARY,30,2024
                """.getBytes(StandardCharsets.UTF_8)
        );
        when(csvFileHandler.readEntries(any(InputStream.class), eq(UpdateTicketEntity.class)))
                .thenThrow(new CsvContentException("Header is missing required fields"));

        CsvContentException exception = Assert.expectThrows(CsvContentException.class, () -> fileService.processTickets(payload));
        Assert.assertTrue(exception.getMessage().contains("Header is missing required fields"));
    }

    @Test
    public void testProcessTicketsWrapsUnexpectedReadFailure() throws Exception {
        MultipartFile payload = mock(MultipartFile.class);
        InputStreamStub inputStream = new InputStreamStub();
        when(payload.getInputStream()).thenReturn(inputStream);
        when(csvFileHandler.readEntries(eq(inputStream), eq(UpdateTicketEntity.class))).thenThrow(new IOException("boom"));

        FileProcessingException exception = Assert.expectThrows(FileProcessingException.class, () -> fileService.processTickets(payload));
        Assert.assertEquals(exception.getMessage(), "Failed to process uploaded CSV file");
    }

    private static final class InputStreamStub extends java.io.InputStream {

        @Override
        public int read() {
            return -1;
        }
    }
}
