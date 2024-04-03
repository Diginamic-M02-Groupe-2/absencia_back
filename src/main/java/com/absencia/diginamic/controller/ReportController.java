package com.absencia.diginamic.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.entity.User.Service;
import com.absencia.diginamic.service.EmployerWtrService;
import com.absencia.diginamic.service.PublicHolidayService;
import com.absencia.diginamic.service.*;

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
	private final DateService dateService;

	public ReportController(final PublicHolidayService publicHolidayService, final EmployerWtrService employerWtrService,
							final AbsenceRequestService absenceRequestService, final DateService dateService) {
		this.publicHolidayService = publicHolidayService;
		this.employerWtrService = employerWtrService;
		this.absenceRequestService = absenceRequestService;
		this.dateService = dateService;
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
	public ResponseEntity<?> getPlanningReport(@RequestParam final int month, @RequestParam final int year, @RequestParam final int service) {

		// Récupérer l'objet Service correspondant à partir de l'ID du service
		Service currentService = absenceRequestService.getServiceById(service);

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