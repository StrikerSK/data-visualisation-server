package com.charts.general.config;

import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.ticket.service.TicketService;
import com.charts.files.controller.FileController;
import com.charts.files.service.FileService;
import com.charts.files.service.IDataGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "com.charts.file.enabled", havingValue = "true")
public class ControllerConfig {

    @Value("${com.charts.file.enabled}")
    private String property;

    @Bean
    public FileService getFileService(CouponV2Service couponService, TicketService ticketService, IDataGenerator dataGenerator) {
        return new FileService(couponService, ticketService, dataGenerator);
    }

    @Bean
    public FileController getFileController(FileService fileService) {
        return new FileController(fileService);
    }

}