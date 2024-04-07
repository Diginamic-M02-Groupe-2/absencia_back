package com.absencia.diginamic.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.entity.User.Employee;
import com.absencia.diginamic.entity.User.Manager;
import com.absencia.diginamic.entity.User.Service;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.service.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
	private final AbsenceRequestService absenceRequestService;
	private final DateService dateService;
	private final EmployerWtrService employerWtrService;
	private final PublicHolidayService publicHolidayService;
	private final UserService userService;

	public ReportController(final AbsenceRequestService absenceRequestService, final DateService dateService, final EmployerWtrService employerWtrService, final PublicHolidayService publicHolidayService, final UserService userService) {
		this.absenceRequestService = absenceRequestService;
		this.dateService = dateService;
		this.employerWtrService = employerWtrService;
		this.publicHolidayService = publicHolidayService;
		this.userService = userService;
	}

	@GetMapping("/planning")
	public ResponseEntity<?> getPlanningReport(final Authentication authentication, @RequestParam final int month, @RequestParam final int year) {
		final User user = userService.loadUserByUsername(authentication.getName());
		final List<AbsenceRequest> absenceRequests = absenceRequestService.findApprovedByMonthYearAndServiceAndEmployees(month, year, user.getService(), List.of(user.getId()));
		final List<EmployerWtr> employerWtr = employerWtrService.findByYear(year);
		final List<PublicHoliday> publicHolidays = publicHolidayService.findByMonthAndYear(month, year);
		final long remainingPaidLeaves = absenceRequestService.countRemainingPaidLeaves(user);
		final long remainingEmployeeWtr = absenceRequestService.countRemainingEmployeeWtr(user);

		return ResponseEntity.ok(Map.of(
			"absenceRequests", absenceRequests,
			"employerWtr", employerWtr,
			"publicHolidays", publicHolidays,
			"remainingPaidLeaves", remainingPaidLeaves,
			"remainingEmployeeWtr", remainingEmployeeWtr
		));
	}

	@GetMapping("/table")
	@Secured("MANAGER")
	public ResponseEntity<?> getTableReport(final Authentication authentication, @RequestParam final int month, @RequestParam final int year, @RequestParam("service") final int serviceId) {
		final Service service = Service.values()[serviceId];

		// Verify that the service exists
		if (service == null) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("message", "Ce service n'existe pas."));
		}

		final Manager manager = (Manager) userService.loadUserByUsername(authentication.getName());
		final List<Employee> employees = manager.getEmployees();
		final List<AbsenceRequest> absenceRequests = absenceRequestService.findApprovedByMonthYearAndServiceAndEmployees(
			month,
			year,
			service,
			employees
				.stream()
				.map(Employee::getId)
				.toList()
		);
		final List<EmployerWtr> employerWtr = employerWtrService.findByYear(year);
		final List<PublicHoliday> publicHolidays = publicHolidayService.findByMonthAndYear(month, year);

		return ResponseEntity.ok(Map.of(
			"absenceRequests", absenceRequests,
			"employerWtr", employerWtr,
			"publicHolidays", publicHolidays
		));
	}

	@GetMapping("/histogram")
	@Secured("MANAGER")
	public ResponseEntity<?> getHistogramReport(@RequestParam final int month, @RequestParam final int year, @RequestParam("service") final int serviceId) {
		final Service service = Service.values()[serviceId];

		// Verify that the service exists
		if (service == null) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("message", "Ce service n'existe pas."));
		}

		final List<EmployerWtr> employees = employerWtrService.findByYear(year);

		final List<AbsenceRequest> absenceRequests = absenceRequestService.findByMonthYearAndService(month, year, service);

		final List<Map<String, Object>> histogramData = new ArrayList<>();

		// Itérer sur chaque employé pour construire les données de l'histogramme
		for (EmployerWtr employee : employees) {
			Map<String, Object> employeeData = new HashMap<>();
			User user = userService.find(employee.getId());
			employeeData.put("id", user.getId());
			employeeData.put("firstName", user.getFirstName());
			employeeData.put("lastName", user.getLastName());

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