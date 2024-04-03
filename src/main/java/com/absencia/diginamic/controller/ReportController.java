package com.absencia.diginamic.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.entity.User.Service;
import com.absencia.diginamic.service.AbsenceRequestService;
import com.absencia.diginamic.service.DateService;
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
	private final DateService dateService;
	private final AbsenceRequestService absenceRequestService;

	public ReportController(final PublicHolidayService publicHolidayService, final EmployerWtrService employerWtrService,
							final DateService dateService, final AbsenceRequestService absenceRequestService) {
		this.publicHolidayService = publicHolidayService;
		this.employerWtrService = employerWtrService;
		this.dateService = dateService;
		this.absenceRequestService = absenceRequestService;
	}

	@GetMapping("/employer-wtr-and-public-holidays")
	public ResponseEntity<Map<String, ?>> getEmployerWtrAndPublicHolidayReport(@RequestParam final int year) {
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
	public ResponseEntity<?> getHistogramReport(@RequestParam final int month, @RequestParam final int year, @RequestParam final int service) {

		Service currentService = absenceRequestService.getServiceById(service);

		if (currentService == null) {
			return ResponseEntity.badRequest().body(Map.of("message", "Ce service n'existe pas."));
		}

		final List<EmployerWtr> employees = employerWtrService.findByYear(year);

		final List<AbsenceRequest> absenceRequests = absenceRequestService.findByMonthYearAndService(month, year, currentService);

		final List<Map<String, Object>> histogramData = new ArrayList<>();

		// Itérer sur chaque employé pour construire les données de l'histogramme
		for (EmployerWtr employee : employees) {
			Map<String, Object> employeeData = new HashMap<>();

			List<Integer> dataSet = new ArrayList<>();

			// Itérer sur chaque jour du mois pour l'employé actuel
			for (int day = 1; day <= dateService.getDaysInMonth(month, year); day++) {
				final int finalDay = day;
				long absenceCount = absenceRequests.stream()
						.filter(request ->
								request.getUser().getId() == employee.getId() &&
										request.getStartedAt().getDayOfMonth() <= finalDay &&
										request.getEndedAt().getDayOfMonth() >= finalDay)
						.count();

				dataSet.add((int) absenceCount);
			}

			employeeData.put("dataset", dataSet);
			histogramData.add(employeeData);
		}

		return ResponseEntity.ok(histogramData);
	}


}