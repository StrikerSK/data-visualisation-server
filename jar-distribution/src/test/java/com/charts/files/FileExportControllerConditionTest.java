package com.charts.files;

import com.charts.main.JavaPidApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = JavaPidApplication.class,
        properties = {
                "com.charts.files.enabled=true",
                "com.charts.files.exports.enabled=false",
                "com.charts.files.imports.enabled=true",
                "spring.liquibase.enabled=false"
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
public class FileExportControllerConditionTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mvc;

//    private byte[] couponInput;
//
//    @BeforeClass
//    public void setUp() throws Exception {
//        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("DummyCouponData.csv");
//        couponInput = IOUtils.toByteArray(inputStream);
//    }

    @Test
    public void testCouponFileExport() throws Exception {
        mvc.perform(get("/file/coupon"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testTicketFileExport() throws Exception {
        mvc.perform(get("/file/ticket"))
                .andExpect(status().isMethodNotAllowed());
    }

//    @Test
//    public void testCouponFileImport() throws Exception {
//        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("DummyCouponData.csv");
//        byte[] couponInput = IOUtils.toByteArray(inputStream);
//        mvc.perform(post("/file/coupon").contentType("multipart/form-data").content(couponInput))
//                .andExpect(status().isNoContent());
//    }

//    @Test
//    public void testTicketFileImport() throws Exception {
//        mvc.perform(post("/file/ticket").content(inputFile).contentType("text/csv"))
//                .andExpect(status().isNoContent());
//    }
}
