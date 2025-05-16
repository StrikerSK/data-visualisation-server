package com.charts.files.controller;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.files.conditions.FileExportCondition;
import com.charts.files.service.FileService;
import com.charts.files.utils.CsvProcessor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Conditional(FileExportCondition.class)
public class FileExportController {

	private final FileService fileService;

	public FileExportController(FileService fileService) {
		this.fileService = fileService;
	}

	@GetMapping(value = "/ticket", produces = "text/csv")
	public void exportTicketsCsv(
			@RequestParam(name = "random", required = false) Boolean random,
			@RequestParam(name = "count", required = false, defaultValue = "100") Integer count,
			HttpServletResponse response
	) {
		List<UpdateTicketEntity> ticketList = fileService.fetchTickets(count, random);
		writeResponse(response, ticketList, "ticket");
	}

	@GetMapping(value = "/coupon", produces = "text/csv")
	public void exportCouponsCsv(
			@RequestParam(name = "random", required = false) Boolean random,
			@RequestParam(name = "count", required = false, defaultValue = "100") Integer count,
			HttpServletResponse response
	) {
		List<UpdateCouponEntity> couponList = fileService.fetchCoupons(count, random);
		writeResponse(response, couponList, "coupon");
	}

	private static <T> void writeResponse(HttpServletResponse response, List<T> entries, String prefix) {
		try {
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s_%s.csv", prefix, UUID.randomUUID()));
			response.setHeader(HttpHeaders.CONTENT_ENCODING, "UTF-8");
			response.setHeader(HttpHeaders.CONTENT_TYPE, "text/csv");
			response.setStatus(HttpServletResponse.SC_OK);
			CsvProcessor.writeEntries(response.getWriter(), entries);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
