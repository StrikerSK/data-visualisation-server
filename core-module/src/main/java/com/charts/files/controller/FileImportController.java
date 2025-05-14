package com.charts.files.controller;

import com.charts.files.conditions.FileImportCondition;
import com.charts.files.service.FileService;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Consumer;

@RestController
@RequestMapping("/file")
@Conditional(FileImportCondition.class)
public class FileImportController {

	private final FileService fileService;

	public FileImportController(FileService fileService) {
		this.fileService = fileService;
	}

	@PostConstruct
	public void init() {
		System.out.println("CouponFileController initialized!");
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

	protected static ResponseEntity<?> processRequest(MultipartFile payload, Consumer<MultipartFile> persistence) {
		persistence.accept(payload);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
