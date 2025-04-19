package com.charts.general.config;

import com.charts.files.controller.FileController;
import com.charts.files.service.FileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    @ConditionalOnProperty(value = "com.charts.file.enabled", havingValue = "true")
    public FileController fileController(FileService fileService) {
        return new FileController(fileService);
    }

}
