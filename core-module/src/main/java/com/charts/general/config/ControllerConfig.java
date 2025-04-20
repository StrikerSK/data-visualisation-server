package com.charts.general.config;

import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.ticket.service.TicketService;
import com.charts.files.controller.FileController;
import com.charts.files.service.FileService;
import com.charts.files.service.IDataGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ControllerConfig {

    @Value("${com.charts.file.enabled}")
    private String property;

    @Bean
    @ConditionalOnProperty(name = "com.charts.file.enabled", havingValue = "true")
    public FileService getFileService(CouponV2Service couponService, TicketService ticketService, IDataGenerator dataGenerator) {
        return new FileService(couponService, ticketService, dataGenerator);
    }

    @Bean
    @ConditionalOnBean(FileService.class)
    public FileController fileController(FileService fileService) {
        return new FileController(fileService);
    }

    @Bean
    @ConditionalOnBean(FileController.class)
    public InitializingBean registerFileControllerMappings(
            RequestMappingHandlerMapping mapping,
            FileController fileController
    ) {
        return () -> {
            register(mapping, fileController, "exportCouponsCsv", RequestMethod.GET, "/file/coupon");
            register(mapping, fileController, "exportTicketsCsv", RequestMethod.GET, "/file/ticket");
            register(mapping, fileController, "uploadCouponsCsv", RequestMethod.POST, "/file/coupon");
            register(mapping, fileController, "uploadTicketsCsv", RequestMethod.POST, "/file/ticket");
        };
    }

    private void register(RequestMappingHandlerMapping mapping, Object handler, String methodName, RequestMethod httpMethod, String path) throws NoSuchMethodException {
        Method method = Arrays.stream(handler.getClass().getMethods())
                .filter(m -> m.getName().equals(methodName))
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Method not found: " + methodName));

        RequestMappingInfo info = RequestMappingInfo
                .paths(path)
                .methods(httpMethod)
                .produces("text/csv") // optional, change if needed
                .build();

        mapping.registerMapping(info, handler, method);
    }

}