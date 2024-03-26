package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.AbsenceRequestStatus;
import com.absencia.diginamic.entity.AbsenceType;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.model.PatchAbsenceRequestModel;
import com.absencia.diginamic.model.PostAbsenceRequestModel;
import com.absencia.diginamic.service.AbsenceRequestService;
import com.absencia.diginamic.service.UserService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

	@GetMapping
	public ResponseEntity<?> getAbsenceRequests(final Authentication authentication) {
		final User user = userService.loadUserByUsername(authentication.getName());
		final List<AbsenceRequest> absenceRequests = absenceRequestService.findByUser(user);

		return ResponseEntity.ok(absenceRequests);
	}

	// TODO: Verify that the start date is not a public holiday, a WTR day or a week-end
	// TODO: Verify that the end date is not a public holiday, a WTR day or a week-end
	@PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> postAbsenceRequest(final Authentication authentication, @ModelAttribute @Valid final PostAbsenceRequestModel model) {
		// Verify that the start date is lesser than the end date
		if (model.getStartedAt().compareTo(model.getEndedAt()) > 0) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("startedAt", "Veuillez sélectionner une période valide."));
		}

		// Verify that reason is not null or empty when the absence type is UNPAID_LEAVE
		if (model.getType() == AbsenceType.UNPAID_LEAVE && (model.getReason() == null || model.getReason().trim().isEmpty())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("reason", "Veuillez spécifier une raison pour votre demande de congés sans solde."));
		}

		final AbsenceRequest absenceRequest = new AbsenceRequest();

		absenceRequest
			.setUser(userService.loadUserByUsername(authentication.getName()))
			.setType(model.getType())
			.setStartedAt(model.getStartedAt())
			.setEndedAt(model.getEndedAt())
			.setReason(model.getReason());

		// Verify that the period does not overlap with another request this user has made
		if (absenceRequestService.isOverlapping(absenceRequest)) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("startedAt", "Cette période est déjà prise. Veuillez en sélectionner une autre."));
		}

		absenceRequestService.save(absenceRequest);

		return ResponseEntity.ok(Map.of("message", "Votre demande d'absence a été créée."));
	}

	// TODO: Verify that the start date is not a public holiday, a WTR day or a week-end
	// TODO: Verify that the end date is not a public holiday, a WTR day or a week-end
	@PatchMapping(value="/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> patchAbsenceRequest(final Authentication authentication, @PathVariable final Long id, @ModelAttribute @Valid final PatchAbsenceRequestModel model) {
		final AbsenceRequest absenceRequest = absenceRequestService.find(id);

		// Verify that this absence request exists and is not deleted
		if (absenceRequest == null || absenceRequest.getDeletedAt() != null) {
			return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message", "Cette demande d'absence n'existe pas ou plus."));
		}

		final User user = userService.loadUserByUsername(authentication.getName());

		// Verify that the absence request is owned by this user
		if (!absenceRequest.getUser().equals(user)) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "Cette demande d'absence ne vous appartient pas."));
		}

		// Verify that the absence request is not waiting for approval
		if (absenceRequest.getStatus() == AbsenceRequestStatus.PENDING) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "Cette demande d'absence est en cours de validation."));
		}

		// Verify that the absence request is not approved
		if (absenceRequest.getStatus() == AbsenceRequestStatus.APPROVED) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "Cette demande d'absence a été validée."));
		}

		final boolean isOverlappingAnotherAbsenceRequest = absenceRequestService.isOverlapping(absenceRequest);

		// Verify that the period does not overlap with another request this user has made
		if (isOverlappingAnotherAbsenceRequest) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("startedAt", "Cette période est déjà prise par une demande d'absence. Veuillez en sélectionner une autre."));
		}

		// Verify that the start date is lesser than the end date
		if (model.getStartedAt().compareTo(model.getEndedAt()) > 0) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("startedAt", "Veuillez sélectionner une période valide."));
		}

		// Verify that reason is not null or empty when the absence type is UNPAID_LEAVE
		if (model.getType() == AbsenceType.UNPAID_LEAVE && (model.getReason() == null || model.getReason().trim().isEmpty())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("reason", "Veuillez spécifier une raison pour votre demande de congés sans solde."));
		}

		absenceRequest
			.setType(model.getType())
			.setStatus(AbsenceRequestStatus.INITIAL)
			.setStartedAt(model.getStartedAt())
			.setEndedAt(model.getEndedAt())
			.setReason(model.getReason());

		absenceRequestService.save(absenceRequest);

		return ResponseEntity.ok(Map.of("message", "La demande d'absence a été modifiée."));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAbsenceRequest(final Authentication authentication, @PathVariable final Long id) {
		final AbsenceRequest absenceRequest = absenceRequestService.find(id);

		// Verify that this absence request exists and is not deleted
		if (absenceRequest == null || absenceRequest.getDeletedAt() != null) {
			return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message", "Cette demande d'absence n'existe pas ou plus."));
		}

		final User user = userService.loadUserByUsername(authentication.getName());

		// Verify that the absence request is owned by this user
		if (!absenceRequest.getUser().equals(user)) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "Cette demande d'absence ne vous appartient pas."));
		}

		absenceRequestService.delete(absenceRequest);

		return ResponseEntity.ok(Map.of("message", "La demande d'absence a été supprimée."));
	}
}