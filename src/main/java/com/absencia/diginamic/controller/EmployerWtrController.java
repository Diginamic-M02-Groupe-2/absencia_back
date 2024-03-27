package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.model.PatchEmployerWtrModel;
import com.absencia.diginamic.model.PostEmployerWtrModel;
import com.absencia.diginamic.service.EmployerWtrService;

import jakarta.validation.Valid;

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
		// TODO: Verify that the user is an administrator

		return ResponseEntity.ok(Map.of("message", "TODO"));
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