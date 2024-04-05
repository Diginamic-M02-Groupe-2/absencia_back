package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.service.EmployerWtrService;
import com.absencia.diginamic.service.PublicHolidayService;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employer-wtr-and-public-holidays")
public class EmployerWtrAndPublicHolidayController {
	private final EmployerWtrService employerWtrService;
	private final PublicHolidayService publicHolidayService;

	public EmployerWtrAndPublicHolidayController(final EmployerWtrService employerWtrService, final PublicHolidayService publicHolidayService) {
		this.employerWtrService = employerWtrService;
		this.publicHolidayService = publicHolidayService;
	}

	@GetMapping("/{year}")
	public ResponseEntity<Map<String, ?>> getEmployerWtrAndPublicHolidays(@PathVariable final int year) {
		final List<EmployerWtr> approvedEmployerWtr = employerWtrService.findApprovedByYear(year);
		final List<PublicHoliday> publicHolidays = publicHolidayService.findByYear(year);

		return ResponseEntity.ok(Map.of(
			"employerWtr", approvedEmployerWtr,
			"publicHolidays", publicHolidays
		));
	}
}