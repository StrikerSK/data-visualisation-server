package com.charts.files.service;

import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.ticket.service.TicketService;
import com.charts.files.exception.CsvContentException;
import com.charts.files.exception.FileProcessingException;
import com.charts.files.generator.IDataGenerator;
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

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileServiceTest {

    @Mock
    private CouponV2Service couponService;

    @Mock
    private TicketService ticketService;

    @Mock
    private IDataGenerator dataGenerator;

    private AutoCloseable closeable;
    private FileService fileService;

    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        fileService = new FileService(couponService, ticketService, dataGenerator);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testProcessCouponsPersistsParsedEntries() {
        MockMultipartFile payload = new MockMultipartFile(
                "payload",
                "CouponData.csv",
                "text/csv",
                """
                MONTH,PERSONTYPE,SELLTYPE,VALIDITY,VALUE,YEAR
                JANUARY,ADULT,CARD,MONTHLY,100,2024
                """.getBytes(StandardCharsets.UTF_8)
        );

        doNothing().when(couponService).saveAll(anyList());

        fileService.processCoupons(payload);

        verify(couponService).saveAll(anyList());
    }

    @Test
    public void testProcessCouponsRejectsMalformedCsv() {
        MockMultipartFile payload = new MockMultipartFile(
                "payload",
                "CouponData.csv",
                "text/csv",
                """
                MONTH,SELLTYPE,VALIDITY,VALUE,YEAR
                January,chip card,monthly,100,2024
                """.getBytes(StandardCharsets.UTF_8)
        );

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
}
