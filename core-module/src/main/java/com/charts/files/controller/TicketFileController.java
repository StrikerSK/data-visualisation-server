package com.charts.files.controller;

import com.charts.api.ticket.entity.v2.UpdateTicketEntity;
import com.charts.files.service.FileService;
import com.charts.general.config.FileCondition;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.charts.files.controller.FileController.processRequest;
import static com.charts.files.controller.FileController.writeResponse;

@RestController
@RequestMapping("/file")
@Conditional(FileCondition.class)
public class TicketFileController {

	private final FileService fileService;

	public TicketFileController(FileService fileService) {
		this.fileService = fileService;
	}

	@PostConstruct
	public void init() {
		System.out.println("TicketFileController initialized!");
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

    @SneakyThrows
    @PostMapping(value = "/ticket", consumes = "multipart/form-data")
	public ResponseEntity<?> uploadTicketsCsv(@RequestParam MultipartFile payload) {
		return processRequest(payload, fileService::processTickets);
	}

}
