package com.absencia.diginamic.controller;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.entity.User.Service;
import com.absencia.diginamic.service.AbsenceRequestService;
import com.absencia.diginamic.service.EmployerWtrService;
import com.absencia.diginamic.service.PublicHolidayService;
import com.absencia.diginamic.service.UserService;
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
	private final AbsenceRequestService absenceRequestService;

	private final UserService userService;

	public ReportController(final PublicHolidayService publicHolidayService, final EmployerWtrService employerWtrService,
							final AbsenceRequestService absenceRequestService, final UserService userService) {
		this.publicHolidayService = publicHolidayService;
		this.employerWtrService = employerWtrService;
		this.absenceRequestService = absenceRequestService;
		this.userService = userService;
	}

	@GetMapping("/employer-wtr-and-public-holidays")
	public ResponseEntity<?> getEmployerWtrAndPublicHolidayReport() {
		// TODO

		return ResponseEntity.ok(Map.of("message", "TODO"));
	}

	@GetMapping("/planning")
	public ResponseEntity<?> getPlanningReport(@RequestParam final int month, @RequestParam final int year, @RequestParam final int service) {

		// Récupérer l'objet Service correspondant à partir de l'ID du service
		Service currentService = getServiceById(service);

		// Vérifiez si le service existe avant de continuer
		if (currentService == null) {
			return ResponseEntity.badRequest().body(Map.of("message", "Ce service n'existe pas."));
		}

		// Récupérer les demandes d'absence de tous les employés pour le mois, l'année et le service sélectionnés
		final List<EmployerWtr> employees = employerWtrService.findByYear(year);

		// Créer une liste pour stocker les données du planning
		final List<Map<String, Object>> planning = new ArrayList<>();

		final List<AbsenceRequest> absenceRequests = absenceRequestService.findByMonthYearAndService(month, year, currentService);

		// Récupérer les jours fériés du mois
		final List<PublicHoliday> publicHolidays = publicHolidayService.findByMonthAndYear(month, year);

		// Itérer sur chaque employé pour construire son jeu de données pour le planning
		for (EmployerWtr employee : employees) {
			Map<String, Object> employeeData = new HashMap<>();

			List<Integer> dataSet = new ArrayList<>();

			// Construire le jeu de données de l'employé pour chaque jour du mois
			for (int day = 1; day <= getDaysInMonth(month, year); day++) {
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

		// Retourner la réponse avec le statut 200 OK
		return ResponseEntity.ok(response);
	}

	// Fonction utilitaire pour obtenir le nombre de jours dans un mois donné
	private int getDaysInMonth(int month, int year) {
		YearMonth yearMonthObject = YearMonth.of(year, month);
		return yearMonthObject.lengthOfMonth();
	}

	public Service getServiceById(int serviceId) {
		for (Service service : Service.values()) {
			if (service.value == serviceId) {
				return service;
			}
		}
		return null;
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