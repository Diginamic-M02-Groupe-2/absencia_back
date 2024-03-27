package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.model.PatchEmployerWtrModel;
import com.absencia.diginamic.model.PostEmployerWtrModel;
import com.absencia.diginamic.service.EmployerWtrService;

import jakarta.validation.Valid;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer-wtr")
public class EmployerWtrController {
	private EmployerWtrService employerWtrService;

	@Autowired
	public EmployerWtrController(final EmployerWtrService employerWtrService) {
		this.employerWtrService = employerWtrService;
	}

	// TODO: Include non-approved employer WTR?
	@GetMapping("/{year}")
	public ResponseEntity<List<EmployerWtr>> getEmployerWtr(@PathVariable final int year) {
		final List<EmployerWtr> employerWtr = employerWtrService.findByYear(year);

		return ResponseEntity.ok(employerWtr);
	}

	@PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> postEmployerWtr(@ModelAttribute @Valid final PostEmployerWtrModel model) {
		// TODO : Gérer les roles admin
		/* Vérification de l'utilisateur administrateur
		if (!userService.isAdmin()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "L'utilisateur n'est pas autorisé à effectuer cette action."));
		}*/

		// Vérification que tous les champs requis sont renseignés
		if (model.getDate() == null || model.getLabel() == null) {
			return ResponseEntity.badRequest().body(Map.of("message", "Tous les champs requis doivent être renseignés."));
		}

		// Vérification que la date n'est pas dans le passé
		LocalDate currentDate = LocalDate.now();
		if (model.getDate().isBefore(currentDate)) {
			return ResponseEntity.badRequest().body(Map.of("message", "La date ne peut pas être dans le passé."));
		}

		// Vérification que la date n'est pas un week-end
		DayOfWeek dayOfWeek = model.getDate().getDayOfWeek();
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			return ResponseEntity.badRequest().body(Map.of("message", "La date ne peut pas être un week-end."));
		}

		// Vérification qu'aucun autre jour férié ou RTT employeur n'existe à cette date
		if (employerWtrService.existsEmployerWtrForDate(model.getDate())) {
			return ResponseEntity.badRequest().body(Map.of("message", "Un autre RTT employeur existe déjà à cette date."));
		}

		// Sauvegarde de l'objet EmployerWtr
		employerWtrService.saveEmployerWtr(model);

		return ResponseEntity.ok(Map.of("message", "La RTT employeur a été créée."));
	}

	@PatchMapping(value="/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> patchEmployerWtr(@PathVariable final long id, @ModelAttribute @Valid final PatchEmployerWtrModel model) {
		// TODO: Verify that the user is an administrator

		return ResponseEntity.ok(Map.of("message", "TODO"));
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity<Map<String, String>> deleteEmployerWtr(@PathVariable final long id) {
		// TODO: Verify that the user is an administrator

		return ResponseEntity.ok(Map.of("message", "TODO"));
	}
}