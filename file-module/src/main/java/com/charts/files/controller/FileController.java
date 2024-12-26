package com.charts.files.controller;

import com.charts.api.coupon.entity.v2.UpdateCouponEntity;
import com.charts.files.service.FileService;
import com.opencsv.CSVWriter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/file")
public class FileController {

	private final FileService fileService;

	@GetMapping("/coupon")
	public void exportToCsv(HttpServletResponse response) {
		try (CSVWriter writer = new CSVWriter(response.getWriter())) {
			response.setContentType("text/csv;charset=UTF-8");
			response.setHeader("Content-Disposition", String.format("attachment; filename=coupons_%s.csv", UUID.randomUUID()));
			response.setStatus(HttpServletResponse.SC_OK);
			fileService.fetchCoupons(writer);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			throw new RuntimeException(e);
        }
    }

	@PostMapping("/coupon")
	public ResponseEntity<List<UpdateCouponEntity>> uploadCsv(@RequestBody String payload) {
		try {
			fileService.processCoupons(payload);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}
