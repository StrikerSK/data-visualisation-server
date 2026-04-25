package com.charts.files.controller;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.files.exception.FileProcessingException;
import com.charts.files.service.FileService;
import com.charts.files.utils.CsvFileHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileExportControllerTest {

    @Mock
    private FileService fileService;

    @Mock
    private CsvFileHandler csvFileHandler;

    @Mock
    private HttpServletResponse response;

    private AutoCloseable closeable;
    private FileExportController controller;

    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        controller = new FileExportController(fileService, csvFileHandler);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testExportCouponsSetsCsvHeadersAndDelegatesWrite() throws IOException {
        StringWriter writer = new StringWriter();
        List<UpdateCouponEntity> coupons = List.of(UpdateCouponEntity.builder().build());
        when(fileService.fetchCoupons(10, false)).thenReturn(coupons);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        controller.exportCouponsCsv(false, 10, response);

        verify(fileService).fetchCoupons(10, false);
        verify(response).setHeader(startsWith("Content-Disposition"), startsWith("attachment; filename=coupon_"));
        verify(response).setHeader("Content-Encoding", "UTF-8");
        verify(response).setHeader("Content-Type", "text/csv");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(csvFileHandler).writeEntries(any(PrintWriter.class), org.mockito.ArgumentMatchers.same(coupons));
    }

    @Test
    public void testExportTicketsSetsCsvHeadersAndDelegatesWrite() throws IOException {
        StringWriter writer = new StringWriter();
        List<UpdateTicketEntity> tickets = List.of(UpdateTicketEntity.builder().build());
        when(fileService.fetchTickets(5, true)).thenReturn(tickets);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        controller.exportTicketsCsv(true, 5, response);

        verify(fileService).fetchTickets(5, true);
        verify(response).setHeader(startsWith("Content-Disposition"), startsWith("attachment; filename=ticket_"));
        verify(response).setHeader("Content-Encoding", "UTF-8");
        verify(response).setHeader("Content-Type", "text/csv");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(csvFileHandler).writeEntries(any(PrintWriter.class), org.mockito.ArgumentMatchers.same(tickets));
    }

    @Test
    public void testExportCouponsWrapsWriterFailure() throws IOException {
        when(fileService.fetchCoupons(10, false)).thenReturn(List.of());
        when(response.getWriter()).thenThrow(new IOException("writer unavailable"));

        FileProcessingException exception = Assert.expectThrows(
                FileProcessingException.class,
                () -> controller.exportCouponsCsv(false, 10, response)
        );

        Assert.assertEquals(exception.getMessage(), "Failed to write CSV response");
    }
}
