package com.absencia.diginamic.controller;

import com.absencia.diginamic.model.PatchPublicHolidayModel;
import com.absencia.diginamic.service.PublicHolidayService;

import jakarta.validation.Valid;

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
	public ResponseEntity<?> getPublicHolidays(@PathVariable final int year) {
		// TODO: Get public holidays that are:
		// - within the range of the provided year
		// - not deleted

		return ResponseEntity.ok(Map.of("message", "TODO"));
	}

	@PatchMapping(value="/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> patchPublicHoliday(@PathVariable final long id, @ModelAttribute @Valid final PatchPublicHolidayModel model) {
		// TODO: Verify that the user is an administrator

		return ResponseEntity.ok(Map.of("message", "TODO"));
	}
}