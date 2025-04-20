package com.charts.files.controller;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.files.service.FileService;
import com.charts.files.utils.CsvProcessor;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@RestController
@RequestMapping("/file")
public class FileController {

	private final FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
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

	@GetMapping(value = "/ticket", produces = "text/csv")
	public void exportTicketsCsv(
			@RequestParam(name = "random", required = false) Boolean random,
			@RequestParam(name = "count", required = false, defaultValue = "100") Integer count,
			HttpServletResponse response
	) {
		List<UpdateTicketEntity> ticketList = fileService.fetchTickets(count, random);
		writeResponse(response, ticketList, "ticket");
	}

	private static <T> void writeResponse(HttpServletResponse response, List<T> entries, String prefix) {
		try  {
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s_%s.csv", prefix, UUID.randomUUID()));
			response.setHeader(HttpHeaders.CONTENT_ENCODING, "UTF-8");
			response.setHeader(HttpHeaders.CONTENT_TYPE, "text/csv");
			response.setStatus(HttpServletResponse.SC_OK);
			CsvProcessor.writeEntries(response.getWriter(), entries);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@SneakyThrows
    @PostMapping(value = "/coupon", consumes = "multipart/form-data")
	public ResponseEntity<?> uploadCouponsCsv(@RequestParam MultipartFile payload) {
		return processRequest(payload, fileService::processCoupons);
	}

    @SneakyThrows
    @PostMapping(value = "/ticket", consumes = "multipart/form-data")
	public ResponseEntity<?> uploadTicketsCsv(@RequestParam MultipartFile payload) {
		return processRequest(payload, fileService::processTickets);
	}

	private static ResponseEntity<?> processRequest(MultipartFile payload, Consumer<MultipartFile> persistence) {
		persistence.accept(payload);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
