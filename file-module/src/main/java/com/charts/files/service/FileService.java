package com.charts.files.service;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.api.ticket.service.TicketService;
import com.charts.files.utils.CsvProcessor;
import com.charts.files.exception.CsvContentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class FileService {

	private final CouponV2Service couponService;
	private final TicketService ticketService;
	private final IDataGenerator dataGenerator;

	public List<UpdateCouponEntity> fetchCoupons(Integer count, Boolean random) {
		if (random != null && random) {
			return dataGenerator.generateCoupons(count);
		} else {
			return couponService.findAll(count);
		}
	}

	public List<UpdateTicketEntity> fetchTickets(Integer count, Boolean random) {
		if (random != null && random) {
			return dataGenerator.generateTickets(count);
		} else {
			return ticketService.findAll(count);
		}
	}

	public void processCoupons(MultipartFile payload) {
		processEntries(payload, UpdateCouponEntity.class, couponService::saveAll);
	}

	public void processTickets(MultipartFile payload) {
		processEntries(payload, UpdateTicketEntity.class, ticketService::saveAll);
	}

	private static <T> void processEntries(
			MultipartFile payload,
			Class<T> clazz,
			Consumer<List<T>> persistence
	) {
		try {
			List<T> coupons = CsvProcessor.readEntries(payload.getInputStream(), clazz);
			persistence.accept(coupons);
		} catch (Exception e) {
			throw new CsvContentException(String.format("Failed to process file: %s", e.getMessage()), e);
		}
	}

}
