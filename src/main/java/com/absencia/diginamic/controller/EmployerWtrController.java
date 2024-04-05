package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.model.PatchEmployerWtrModel;
import com.absencia.diginamic.model.PostEmployerWtrModel;
import com.absencia.diginamic.service.EmployerWtrService;
import com.absencia.diginamic.service.PublicHolidayService;

import jakarta.validation.Valid;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer-wtr")
public class EmployerWtrController {
	private final EmployerWtrService employerWtrService;
	private final PublicHolidayService publicHolidayService;

	public EmployerWtrController(final EmployerWtrService employerWtrService, final PublicHolidayService publicHolidayService) {
		this.employerWtrService = employerWtrService;
		this.publicHolidayService = publicHolidayService;
	}

	@GetMapping("/{year}")
	public ResponseEntity<List<EmployerWtr>> getEmployerWtr(@PathVariable final int year) {
		final List<EmployerWtr> employerWtr = employerWtrService.findApprovedByYear(year);

		return ResponseEntity.ok(employerWtr);
	}

	@PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	@Secured("ADMINISTRATOR")
	public ResponseEntity<Map<String, String>> postEmployerWtr(@ModelAttribute @Valid final PostEmployerWtrModel model) {
		// Verify that the date is not passed
		if (model.getDate().isBefore(LocalDate.now())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("date", "Cette date ne doit pas être dans le passé."));
		}

		final DayOfWeek dayOfWeek = model.getDate().getDayOfWeek();

		// Verify that the date is not within a week-end
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("date", "Cette date ne doit pas être un week-end."));
		}

		// Verify that the date is not conflicting with another employer WTR
		if (employerWtrService.isDateConflicting(model.getDate())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("date", "Cette date est déjà prise par une autre RTT employeur."));
		}

		// Verify that the date is not conflicting with a public holiday
		if (publicHolidayService.isDateConflicting(model.getDate())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("date", "Cette date ne doit pas être un jour férié."));
		}

		final EmployerWtr employerWtr = new EmployerWtr();

		employerWtr
			.setDate(model.getDate())
			.setLabel(model.getLabel());

		employerWtrService.save(employerWtr);

		return ResponseEntity.ok(Map.of("message", "La RTT employeur a été créée."));
	}

	@PatchMapping(value="/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	@Secured("ADMINISTRATOR")
	public ResponseEntity<Map<String, String>> patchEmployerWtr(@PathVariable final long id, @ModelAttribute @Valid final PatchEmployerWtrModel model) {
		// Verify that the date is not passed
		if (model.getDate().isBefore(LocalDate.now())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("date", "Cette date ne doit pas être dans le passé."));
		}

		final DayOfWeek dayOfWeek = model.getDate().getDayOfWeek();

		// Verify that the date is not within a week-end
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("date", "Cette date ne doit pas être un week-end."));
		}

		// Verify that the date is not conflicting with another employer WTR
		if (employerWtrService.isDateConflictingWithOther(id, model.getDate())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("date", "Cette date est déjà prise par une autre RTT employeur."));
		}

		// Verify that the date is not conflicting with a public holiday
		if (publicHolidayService.isDateConflicting(model.getDate())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("date", "Cette date ne doit pas être un jour férié."));
		}

		final EmployerWtr employerWtr = employerWtrService.findOneByIdAndDeletedAtIsNull(id);

		// Verify that the employer WTR exists
		if (employerWtr == null) {
			return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message", "Cette RTT employeur n'existe pas ou plus."));
		}

		// Verify that the employer WTR date is not passed
		if (employerWtr.getDate().isBefore(LocalDate.now())) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "Cette RTT employeur est déjà passée."));
		}

		employerWtr
			.setDate(model.getDate())
			.setLabel(model.getLabel());

		employerWtrService.save(employerWtr);

		return ResponseEntity.ok(Map.of("message", "La RTT employeur a été modifiée."));
	}

	@Secured("ADMINISTRATOR")
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Map<String, String>> deleteEmployerWtr(final Authentication authentication, @PathVariable final long id) {
		final EmployerWtr employerWtr = employerWtrService.findOneByIdAndDeletedAtIsNull(id);

		// Verify that the employer WTR exists
		if (employerWtr == null) {
			return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message", "Cette RTT employeur n'existe pas ou plus."));
		}

		// Verify that the employer WTR date is not passed
		if (employerWtr.getDate().isBefore(LocalDate.now())) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "Cette RTT employeur est déjà passée."));
		}

		employerWtrService.delete(employerWtr);

		return ResponseEntity.ok(Map.of("message", "La RTT employeur a été supprimée."));
	}
}