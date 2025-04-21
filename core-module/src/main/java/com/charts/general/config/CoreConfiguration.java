package com.charts.general.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.charts.api", "com.charts.general"})
public class CoreConfiguration {
}
