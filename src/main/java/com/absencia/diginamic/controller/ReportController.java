package com.absencia.diginamic.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.entity.User.Service;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.service.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
	private final PublicHolidayService publicHolidayService;
	private final EmployerWtrService employerWtrService;
	private final AbsenceRequestService absenceRequestService;
	private final DateService dateService;
	private final UserService userService;

	public ReportController(final PublicHolidayService publicHolidayService, final EmployerWtrService employerWtrService, final AbsenceRequestService absenceRequestService, final DateService dateService, final UserService userService) {
		this.publicHolidayService = publicHolidayService;
		this.employerWtrService = employerWtrService;
		this.absenceRequestService = absenceRequestService;
		this.dateService = dateService;
		this.userService = userService;
	}

	@GetMapping("/employer-wtr-and-public-holidays")
	public ResponseEntity<Map<String, ?>> getEmployerWtrAndPublicHolidayReport(@RequestParam final int year) {
		final List<EmployerWtr> employerWtr = employerWtrService.findApprovedByYear(year);
		final List<PublicHoliday> publicHolidays = publicHolidayService.findByYear(year);
		final Map<String, Object> response = new HashMap<>();

		response.put("employerWtr", employerWtr);
		response.put("publicHolidays", publicHolidays);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/planning")
	public ResponseEntity<?> getPlanningReport(@RequestParam final int month, @RequestParam final int year, @RequestParam("service") final int serviceId) {
		// Récupérer l'objet Service correspondant à partir de l'ID du service
		final Service service = Service.values()[serviceId];

		// Verify that the service exists
		if (service == null) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("message", "Ce service n'existe pas."));
		}

		// Récupérer les demandes d'absence de tous les employés pour le mois, l'année et le service sélectionnés
		final List<EmployerWtr> employees = employerWtrService.findApprovedByYear(year);

		// Créer une liste pour stocker les données du planning
		final List<Map<String, Object>> planning = new ArrayList<>();

		final List<AbsenceRequest> absenceRequests = absenceRequestService.findByMonthYearAndService(month, year, service);

		// Récupérer les jours fériés du mois
		final List<PublicHoliday> publicHolidays = publicHolidayService.findByMonthAndYear(month, year);

		// Itérer sur chaque employé pour construire son jeu de données pour le planning
		for (EmployerWtr employee : employees) {
			Map<String, Object> employeeData = new HashMap<>();
			User user = userService.find(employee.getId());
			employeeData.put("id", user.getId());
			employeeData.put("firstName", user.getFirstName());
			employeeData.put("lastName", user.getLastName());

			List<Integer> dataSet = new ArrayList<>();

			// Construire le jeu de données de l'employé pour chaque jour du mois
			for (int day = 1; day <= dateService.getDaysInMonth(month, year); day++) {
				// Vérifier si le jour est un jour férié
				int finalDay = day;
				boolean isHoliday = publicHolidays.stream().anyMatch(holiday -> holiday.getDate().getDayOfMonth() == finalDay);
				if (isHoliday) {
					// Si c'est un jour férié, marquer comme tel dans le jeu de données
					dataSet.add(0);
				} else {
					// Sinon, vérifier s'il y a une demande d'absence pour cet employé ce jour-là
					boolean hasAbsence = absenceRequests.stream().anyMatch(request ->
							request.getUser().getId() == employee.getId() &&
									request.getStartedAt().getDayOfMonth() <= finalDay &&
									request.getEndedAt().getDayOfMonth() >= finalDay);
					if (hasAbsence) {
						// Si une demande d'absence est présente, marquer comme tel dans le jeu de données
						dataSet.add(0);
					} else {
						// Sinon, marquer comme jour de travail normal
						dataSet.add(1);
					}
				}
			}

			employeeData.put("dataset", dataSet);
			planning.add(employeeData);
		}

		// Créer la réponse JSON
		Map<String, Object> response = new HashMap<>();
		response.put("employees", planning);
		response.put("publicHolidays", publicHolidays);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/table")
	@Secured("MANAGER")
	public ResponseEntity<?> getTableReport(@RequestParam final int month, @RequestParam final int year, @RequestParam("service") final int serviceId, final Authentication authentication) {
		final User manager = userService.loadUserByUsername(authentication.getName());
		final Service service = Service.values()[serviceId];

		if (service == null) {
			return ResponseEntity
					.badRequest()
					.body(Map.of("message", "Ce service n'existe pas."));
		}

		List<User> managerEmployees = userService.findEmployeesManagedByManager(manager.getId());

		List<AbsenceRequest> absenceRequests = absenceRequestService.findApprovedByMonthYearAndServiceAndEmployees(month, year, service, managerEmployees);

		List<EmployerWtr> approvedEmployerWtr = employerWtrService.findApprovedByYearAndEmployees(year, managerEmployees);

		Map<String, Object> responseData = new HashMap<>();
		responseData.put("absenceRequests", absenceRequests);
		responseData.put("employerWtr", approvedEmployerWtr);

		return ResponseEntity.ok(responseData);
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

		final List<EmployerWtr> employees = employerWtrService.findApprovedByYear(year);

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