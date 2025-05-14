package com.charts.files.controller;

import com.charts.files.utils.CsvProcessor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

class FileController {

	protected static <T> void writeResponse(HttpServletResponse response, List<T> entries, String prefix) {
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

	protected static ResponseEntity<?> processRequest(MultipartFile payload, Consumer<MultipartFile> persistence) {
		persistence.accept(payload);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
