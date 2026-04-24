package com.charts.files.controller;

import com.charts.files.exception.FileProcessingException;
import com.charts.files.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class FileExportControllerTest {

    @Mock
    private FileService fileService;

    @Mock
    private HttpServletResponse response;

    private AutoCloseable closeable;
    private FileExportController controller;

    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        controller = new FileExportController(fileService);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testExportCouponsWrapsWriterFailure() throws IOException {
        when(fileService.fetchCoupons(10, false)).thenReturn(java.util.List.of());
        when(response.getWriter()).thenThrow(new IOException("writer unavailable"));

        FileProcessingException exception = Assert.expectThrows(
                FileProcessingException.class,
                () -> controller.exportCouponsCsv(false, 10, response)
        );

        Assert.assertEquals(exception.getMessage(), "Failed to write CSV response");
    }
}
