package com.charts.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.charts")
@EnableJpaRepositories(basePackages = {"com.charts.api.coupon.repository", "com.charts.api.ticket.repository"})
@EntityScan(basePackages = {"com.charts.api.coupon.entity", "com.charts.api.ticket.entity", "com.charts.general.entity"})
public class JavaPidApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaPidApplication.class, args);
	}

}
