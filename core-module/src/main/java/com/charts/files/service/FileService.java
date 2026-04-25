package com.charts.files.service;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.coupon.service.CouponV2Service;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.api.ticket.service.TicketService;
import com.charts.files.generator.IDataGenerator;
import com.charts.files.utils.CsvFileHandler;
import com.charts.files.exception.CsvContentException;
import com.charts.files.conditions.FileCondition;
import com.charts.files.exception.FileProcessingException;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Consumer;

@Service
@Conditional(FileCondition.class)
public class FileService {

	private final CouponV2Service couponService;
	private final TicketService ticketService;
	private final IDataGenerator dataGenerator;
    private final CsvFileHandler csvFileHandler;

	public FileService(CouponV2Service couponService, TicketService ticketService, IDataGenerator dataGenerator, CsvFileHandler csvFileHandler) {
		this.couponService = couponService;
		this.ticketService = ticketService;
		this.dataGenerator = dataGenerator;
        this.csvFileHandler = csvFileHandler;
	}

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

	private <T> void processEntries(
			MultipartFile payload,
			Class<T> clazz,
			Consumer<List<T>> persistence
	) {
		try {
			persistence.accept(readEntries(payload, clazz));
		} catch (CsvContentException e) {
			throw e;
		} catch (Exception e) {
			throw new FileProcessingException("Failed to process uploaded CSV file", e);
		}
	}

	private <T> List<T> readEntries(MultipartFile payload, Class<T> clazz) throws Exception {
		return csvFileHandler.readEntries(payload.getInputStream(), clazz);
	}

}
