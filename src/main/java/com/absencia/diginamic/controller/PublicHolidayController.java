package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.model.PatchPublicHolidayModel;
import com.absencia.diginamic.service.PublicHolidayService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public-holidays")
public class PublicHolidayController {
	private PublicHolidayService publicHolidayService;

	@Autowired
	public PublicHolidayController(final PublicHolidayService publicHolidayService) {
		this.publicHolidayService = publicHolidayService;
	}

	@GetMapping("/{year}")
	public ResponseEntity<List<PublicHoliday>> getPublicHolidays(@PathVariable final int year) {
		final List<PublicHoliday> publicHolidays = publicHolidayService.findByYear(year);

		return ResponseEntity.ok(publicHolidays);
	}

	@PatchMapping(value="/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> patchPublicHoliday(@PathVariable final long id, @ModelAttribute @Valid final PatchPublicHolidayModel model) {
		// TODO: Verify that the user is an administrator
		/* Vérification de l'utilisateur administrateur
		if (!userService.isAdmin()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "L'utilisateur n'est pas autorisé à effectuer cette action."));
		}*/

		Optional<PublicHoliday> publicHolidayOptional = publicHolidayService.findById(id);
		if (publicHolidayOptional.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "Le jour férié n'existe pas'."));
		}

		PublicHoliday publicHoliday = publicHolidayOptional.get();

		// Vérification de la date dans le passé
		if (publicHoliday.getDate().isBefore(LocalDate.now())) {
			return ResponseEntity.badRequest().body(Map.of("message", "Le jour férié ne peut pas être dans le passé."));
		}

		// Mise à jour du statut du jour férié
		publicHoliday.setWorked(model.isWorked());
		publicHolidayService.save(publicHoliday);

		return ResponseEntity.ok(Map.of("message", "Le jour férié a été modifié."));
	}
}