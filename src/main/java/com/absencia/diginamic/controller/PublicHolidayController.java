package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.model.PatchPublicHolidayModel;
import com.absencia.diginamic.service.PublicHolidayService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

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

		return ResponseEntity.ok(Map.of("message", "TODO"));
	}
}