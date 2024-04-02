package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.entity.User.Role;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.model.PatchEmployerWtrModel;
import com.absencia.diginamic.model.PostEmployerWtrModel;
import com.absencia.diginamic.service.EmployerWtrService;
import com.absencia.diginamic.service.UserService;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;

import jakarta.validation.Valid;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer-wtr")
public class EmployerWtrController {
	private EmployerWtrService employerWtrService;
	private UserService userService;

	@Autowired
	public EmployerWtrController(final EmployerWtrService employerWtrService,  final UserService userService) {
		this.employerWtrService = employerWtrService;
		this.userService = userService;

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

		// Vérification que la date n'est pas dans le passé
		LocalDate currentDate = LocalDate.now();
		if (model.getDate().isBefore(currentDate)) {
			return ResponseEntity.badRequest().body(Map.of("date", "La date ne peut pas être dans le passé."));
		}

		// Vérification que la date n'est pas un week-end
		DayOfWeek dayOfWeek = model.getDate().getDayOfWeek();
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			return ResponseEntity.badRequest().body(Map.of("date", "La date ne peut pas être un week-end."));
		}

		// Vérification qu'aucun autre jour férié ou RTT employeur n'existe à cette date
		if (employerWtrService.isDateConflicting(model.getDate())) {
			return ResponseEntity.badRequest().body(Map.of("date", "Une autre RTT employeur existe déjà à cette date."));
		}

		// Sauvegarde de l'objet EmployerWtr
		final EmployerWtr employerWtr = new EmployerWtr();
		employerWtr
				.setDate(model.getDate())
				.setLabel(model.getLabel());
		employerWtrService.save(employerWtr);

		return ResponseEntity.ok(Map.of("message", "La RTT employeur a été créée."));
	}

	@PatchMapping(value="/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> patchEmployerWtr(@PathVariable final long id, @ModelAttribute @Valid final PatchEmployerWtrModel model) {

		// TODO : Gérer les roles admin
		/* Vérification de l'utilisateur administrateur
		if (!userService.isAdmin()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "L'utilisateur n'est pas autorisé à effectuer cette action."));
		}*/

		// Vérification de la date dans le passé
		LocalDate currentDate = LocalDate.now();
		if (model.getDate().isBefore(currentDate)) {
			return ResponseEntity.badRequest().body(Map.of("date", "La date ne peut pas être dans le passé."));
		}

		// Vérification de la date un week-end
		DayOfWeek dayOfWeek = model.getDate().getDayOfWeek();
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			return ResponseEntity.badRequest().body(Map.of("date", "La date ne peut pas être un week-end."));
		}

		// Vérification qu'aucun RTT employeur n'existe à cette date
		if (employerWtrService.isDateConflictingWithOther(id, model.getDate())) {
			return ResponseEntity.badRequest().body(Map.of("message", "Une autre RTT employeur existe déjà à cette date."));
		}

		final EmployerWtr employerWtr = employerWtrService.findOneByIdAndDeletedAtIsNull(id);

		// Mise à jour des champs
		employerWtr
				.setDate(model.getDate())
				.setLabel(model.getLabel());

		// Enregistrement des modifications
		employerWtrService.save(employerWtr);

		return ResponseEntity.ok(Map.of("message", "La RTT employeur a été modifiée."));
	}

	@Secured("ADMINISTRATOR")
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Map<String, String>> deleteEmployerWtr(final Authentication authentication, @PathVariable final long id) {
		EmployerWtr employerWtr = employerWtrService.findOneByIdAndDeletedAtIsNull(id);
		if (employerWtr == null) {
			return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message", "Ce RTT employeur n'existe pas ou plus."));
		}

		//Récupérer la date actuelle
		LocalDate currentDate = LocalDate.now();
		// Vérifier que la date de la RTT employeur est passée
		if (employerWtr.getDate().isBefore(currentDate)) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "La date du RTT employeur est passé."));
		}

		// Supprimer la RTT employeur
		employerWtrService.delete(employerWtr);

		return ResponseEntity.ok(Map.of("message", "Le RTT employeur a été supprimé."));
	}
}