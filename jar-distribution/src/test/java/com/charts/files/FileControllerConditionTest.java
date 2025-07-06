package com.charts.files;

import com.charts.main.JavaPidApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(
        classes = JavaPidApplication.class,
        properties = {
                "com.charts.files.enabled=false",
                "spring.liquibase.enabled=false"
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
public class FileControllerConditionTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testCouponFileExport() throws Exception {
        mvc.perform(get("/file/coupon"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testTicketFileExport() throws Exception {
        mvc.perform(get("/file/ticket"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCouponFileImport() throws Exception {
        mvc.perform(post("/file/coupon"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testTicketFileImport() throws Exception {
        mvc.perform(post("/file/ticket"))
                .andExpect(status().isNotFound());
    }
}
