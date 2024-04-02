package com.absencia.diginamic.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.service.EmployerWtrService;
import com.absencia.diginamic.service.PublicHolidayService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
	private final PublicHolidayService publicHolidayService;
	private final EmployerWtrService employerWtrService;

	public ReportController(final PublicHolidayService publicHolidayService, final EmployerWtrService employerWtrService) {
		this.publicHolidayService = publicHolidayService;
		this.employerWtrService = employerWtrService;
	}

	@GetMapping("/employer-wtr-and-public-holidays")
	public ResponseEntity<?> getEmployerWtrAndPublicHolidayReport(@RequestParam final int year) {
		List<EmployerWtr> employerWtr = employerWtrService.findByYear(year);
		List<PublicHoliday> publicHolidays = publicHolidayService.findByYear(year);

		Map<String, Object> response = new HashMap<>();
		response.put("employerWtr", employerWtr);
		response.put("publicHolidays", publicHolidays);

		return ResponseEntity.ok(response);
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