package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.model.PatchPublicHolidayModel;
import com.absencia.diginamic.service.PublicHolidayService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public-holidays")
public class PublicHolidayController {
	private final PublicHolidayService publicHolidayService;

	public PublicHolidayController(final PublicHolidayService publicHolidayService) {
		this.publicHolidayService = publicHolidayService;
	}

	@GetMapping("/{year}")
	public ResponseEntity<List<PublicHoliday>> getPublicHolidays(@PathVariable final int year) {
		final List<PublicHoliday> publicHolidays = publicHolidayService.findByYear(year);

		return ResponseEntity.ok(publicHolidays);
	}

	@PatchMapping(value="/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	@Secured("ADMINISTRATOR")
	public ResponseEntity<Map<String, String>> patchPublicHoliday(@PathVariable final long id, @ModelAttribute @Valid final PatchPublicHolidayModel model) {
		final PublicHoliday publicHoliday = publicHolidayService.findOneById(id);

		if (publicHoliday == null) {
			return ResponseEntity.badRequest().body(Map.of("message", "Ce jour férié n'existe pas."));
		}

		// Vérification de la date dans le passé
		if (publicHoliday.getDate().isBefore(LocalDate.now())) {
			return ResponseEntity.badRequest().body(Map.of("message", "Ce jour férié est déjà passé."));
		}

		// Mise à jour du statut du jour férié
		publicHoliday.setWorked(model.isWorked());

		publicHolidayService.save(publicHoliday);

		return ResponseEntity.ok(Map.of("message", "Le jour férié a été modifié."));
	}
}