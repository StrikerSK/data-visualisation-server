package com.charts.general.config;

import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.ticket.service.TicketService;
import com.charts.files.controller.FileController;
import com.charts.files.service.FileService;
import com.charts.files.service.IDataGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(FileCondition.class)
public class FileConfiguration {

    @Bean
    public FileService fileService(CouponV2Service couponV2Service, TicketService ticketService, IDataGenerator dataGenerator) {
        return new FileService(couponV2Service, ticketService, dataGenerator);
    }

    @Bean
    public FileController fileController(FileService fileService) {
        return new FileController(fileService);
    }

}