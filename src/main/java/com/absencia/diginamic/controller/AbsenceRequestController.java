package com.absencia.diginamic.controller;

import com.absencia.diginamic.dto.PostAbsenceRequestRequest;
import com.absencia.diginamic.model.Absence;
import com.absencia.diginamic.model.AbsenceRequest;
import com.absencia.diginamic.model.AbsenceRequestStatus;
import com.absencia.diginamic.model.AbsenceType;
import com.absencia.diginamic.model.User;
import com.absencia.diginamic.service.AbsenceRequestService;
import com.absencia.diginamic.service.UserService;
import com.absencia.diginamic.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/absence-requests")
public class AbsenceRequestController {
	private AbsenceRequestService absenceRequestService;
	private UserService userService;

	@Autowired
	public AbsenceRequestController(final AbsenceRequestService absenceRequestService, final UserService userService) {
		this.absenceRequestService = absenceRequestService;
		this.userService = userService;
	}

	@GetMapping("/{id}")
	@JsonView(View.AbsenceRequest.class)
	public ResponseEntity<?> getAbsenceRequests(@PathVariable final Long id) {
		final User user = userService.find(id);

		// Verify that the user exists and is not deleted
		if (user == null || !user.isEnabled()) {
			return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message", "L'utilisateur fourni n'existe pas"));
		}

		final List<AbsenceRequest> absencesRequests = absenceRequestService.findByUser(user);

		return ResponseEntity.ok(absencesRequests);
	}

	// TODO: Verify that the start date is not a public holiday, a TOIL day or a week-end
	// TODO: Verify that the end date is not a public holiday, a TOIL day or a week-end
	@PostMapping(value="", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> postAbsenceRequest(@ModelAttribute @Valid final PostAbsenceRequestRequest request) {
		// Verify that the start date is lesser than the end date
		if (request.getStartedAt().compareTo(request.getEndedAt()) > 0) {
			return ResponseEntity
					.badRequest()
					.body(Map.of("startedAt", "Veuillez sélectionner une période valide."));
		}

		// Verify that reason is not null when the absence type is UNPAID_LEAVE
		if (request.getType() == AbsenceType.UNPAID_LEAVE && (request.getReason() == null || request.getReason().trim().isEmpty())) {
			return ResponseEntity
					.badRequest()
					.body(Map.of("reason", "Veuillez spécifier une raison pour votre demande de congés sans solde."));
		}

		final User user = userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		final Absence absence = new Absence()
				.setStartedAt(request.getStartedAt())
				.setEndedAt(request.getEndedAt())
				.setType(request.getType());
		final AbsenceRequest absenceRequest = new AbsenceRequest()
				.setUser(user)
				.setAbsence(absence)
				.setReason(request.getReason())
				.setStatus(AbsenceRequestStatus.INITIAL);

		// Verify that the period does not overlap with another existing request this user has made
		final boolean isOverlapping = absenceRequestService.isOverlapping(absenceRequest);

		if (isOverlapping) {
			return ResponseEntity
					.badRequest()
					.body(Map.of("startedAt", "Cette période est déjà prise par une demande d'absence. Veuillez en sélectionner une autre."));
		}

		absenceRequestService.save(absenceRequest);

		return ResponseEntity.ok(Map.of("message", "Votre demande d'absence a été créée."));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAbsenceRequest(@PathVariable final Long id) {
		final AbsenceRequest absenceRequest = absenceRequestService.find(id);

		if (absenceRequest == null || absenceRequest.getDeletedAt() != null) {
			return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("message", "La demande d'absence n'existe pas ou plus."));
		}

		absenceRequestService.delete(absenceRequest);

		return ResponseEntity.ok(Map.of("message", "La demande d'absence a été supprimée."));
	}
}