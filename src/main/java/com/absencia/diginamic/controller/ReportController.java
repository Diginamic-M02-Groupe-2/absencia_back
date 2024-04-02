package com.absencia.diginamic.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
	public ReportController() {
		//
	}

	@GetMapping("/employer-wtr-and-public-holidays")
	public ResponseEntity<?> getEmployerWtrAndPublicHolidayReport() {
		// TODO

		return ResponseEntity.ok(Map.of("message", "TODO"));
	}

	@GetMapping("/planning")
	public ResponseEntity<?> getPlanningReport() {
		// TODO

		return ResponseEntity.ok(Map.of("message", "TODO"));
	}

	@GetMapping("/table")
	@Secured("MANAGER")
	public ResponseEntity<?> getTableReport() {
		// TODO

		return ResponseEntity.ok(Map.of("message", "TODO"));
	}

	@GetMapping("/histogram")
	@Secured("MANAGER")
	public ResponseEntity<?> getHistogramReport() {
		// TODO

		return ResponseEntity.ok(Map.of("message", "TODO"));
	}
}