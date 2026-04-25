package com.charts.files.controller;

import com.charts.files.service.FileService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.verify;

public class FileImportControllerTest {

    @Mock
    private FileService fileService;

    private AutoCloseable closeable;
    private FileImportController controller;

    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        controller = new FileImportController(fileService);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testUploadCouponsReturnsNoContent() {
        MockMultipartFile payload = new MockMultipartFile(
                "payload",
                "CouponData.csv",
                "text/csv",
                "MONTH,PERSONTYPE,SELLTYPE,VALIDITY,VALUE,YEAR".getBytes(StandardCharsets.UTF_8)
        );

        ResponseEntity<?> response = controller.uploadCouponsCsv(payload);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        verify(fileService).processCoupons(payload);
    }

    @Test
    public void testUploadTicketsReturnsNoContent() {
        MockMultipartFile payload = new MockMultipartFile(
                "payload",
                "TicketData.csv",
                "text/csv",
                "MONTH,TICKETTYPE,DISCOUNTED,VALUE,YEAR".getBytes(StandardCharsets.UTF_8)
        );

        ResponseEntity<?> response = controller.uploadTicketsCsv(payload);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        verify(fileService).processTickets(payload);
    }
}
