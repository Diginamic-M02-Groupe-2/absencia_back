package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.AbsenceRequestStatus;
import com.absencia.diginamic.entity.AbsenceType;
import com.absencia.diginamic.entity.User.Employee;
import com.absencia.diginamic.entity.User.Manager;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.model.PatchAbsenceRequestModel;
import com.absencia.diginamic.model.PostAbsenceRequestModel;
import com.absencia.diginamic.service.*;
import com.absencia.diginamic.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/absence-requests")
public class AbsenceRequestController {
	private final AbsenceRequestService absenceRequestService;
	private final UserService userService;
	private final EmployerWtrService employerWtrService;
	private final PublicHolidayService publicHolidayService;
	private final DateService weekEndService;

	public AbsenceRequestController(
		final AbsenceRequestService absenceRequestService,
		final UserService userService,
		final EmployerWtrService employerWtrService,
		final PublicHolidayService publicHolidayService,
		final DateService weekEndService
	) {
		this.absenceRequestService = absenceRequestService;
		this.userService = userService;
		this.employerWtrService = employerWtrService;
		this.publicHolidayService = publicHolidayService;
		this.weekEndService = weekEndService;
	}

	@GetMapping
	public ResponseEntity<Map<String, ?>> getAbsenceRequests(final Authentication authentication) {
		final User user = userService.loadUserByUsername(authentication.getName());
		final List<AbsenceRequest> absenceRequests = absenceRequestService.findByUser(user);
		final long remainingPaidLeaves = absenceRequestService.countRemainingPaidLeaves(user);
		final long remainingEmployeeWtr = absenceRequestService.countRemainingEmployeeWtr(user);

		return ResponseEntity.ok(Map.of(
			"absenceRequests", absenceRequests,
			"remainingPaidLeaves", remainingPaidLeaves,
			"remainingEmployeeWtr", remainingEmployeeWtr
		));
	}

	@GetMapping("/manager")
	@Secured("MANAGER")
	@JsonView(View.AbsenceRequest.GetEmployeeAbsenceRequests.class)
	public ResponseEntity<List<?>> getEmployeeAbsenceRequests(final Authentication authentication) {
		final Manager manager = (Manager) userService.loadUserByUsername(authentication.getName());
		final List<Employee> employees = manager.getEmployees();
		final List<?> absenceRequestsByEmployee = List.of(
			employees
				.stream()
				.filter(employee -> employee.getService() == manager.getService())
				.map(employee -> Map.of(
					"id", employee.getId(),
					"firstName", employee.getFirstName(),
					"lastName", employee.getLastName(),
					"absenceRequests", absenceRequestService
						.findByUser(employee)
						.stream()
						.filter(absenceRequest -> absenceRequest.getStatus() == AbsenceRequestStatus.PENDING ||
							absenceRequest.getStatus() == AbsenceRequestStatus.REJECTED)
						.toArray()
				))
				.toArray()
		);

		return ResponseEntity.ok(absenceRequestsByEmployee);
	}

	@PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> postAbsenceRequest(final Authentication authentication, @ModelAttribute @Valid final PostAbsenceRequestModel model) {
		// Verify that the start date is lesser than the end date
		if (model.getStartedAt().compareTo(model.getEndedAt()) > 0) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("startedAt", "Veuillez sélectionner une période valide."));
		}

		if (weekEndService.isWeekEndDay(model.getStartedAt())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("startedAt", "La date ne peut pas être un week-end."));
		}

		if (weekEndService.isWeekEndDay(model.getEndedAt())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("endedAt", "La date ne peut pas être un week-end."));
		}

		if (publicHolidayService.isDateConflicting(model.getStartedAt())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("startedAt", "La date sélectionnée est un jour férié."));
		}

		if (publicHolidayService.isDateConflicting(model.getEndedAt())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("endedAt", "La date sélectionnée est un jour férié."));
		}

		if (employerWtrService.isDateConflicting(model.getStartedAt())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("startedAt", "La date sélectionnée est une RTT employeur."));
		}

		if (employerWtrService.isDateConflicting(model.getEndedAt())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("endedAt", "La date sélectionnée es une RTT employeur."));
		}

		// Verify that reason is not null or empty when the absence type is UNPAID_LEAVE
		if (model.getType() == AbsenceType.UNPAID_LEAVE && (model.getReason() == null || model.getReason().trim().isEmpty())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("reason", "Veuillez spécifier un motif pour votre demande de congés sans solde."));
		}

		final User user = userService.loadUserByUsername(authentication.getName());
		final AbsenceRequest absenceRequest = new AbsenceRequest();

		absenceRequest
			.setUser(user)
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

	@PatchMapping(value="/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> patchAbsenceRequest(final Authentication authentication, @PathVariable final long id, @ModelAttribute @Valid final PatchAbsenceRequestModel model) {
		final AbsenceRequest absenceRequest = absenceRequestService.find(id);

		// Verify that this absence request exists
		if (absenceRequest == null) {
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

		if(weekEndService.isWeekEndDay(model.getStartedAt())){
			return ResponseEntity
				.badRequest()
				.body(Map.of("startedAt", "Cette date ne peut pas être un week-end."));
		}

		if(weekEndService.isWeekEndDay(model.getEndedAt())){
			return ResponseEntity
				.badRequest()
				.body(Map.of("endedAt", "Cette date ne peut pas être un week-end."));
		}

		if (publicHolidayService.isDateConflicting(model.getStartedAt())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("startedAt", "La date sélectionnée est un jour férié."));
		}

		if (publicHolidayService.isDateConflicting(model.getEndedAt())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("endedAt", "La date sélectionnée est un jour férié."));
		}

		if (employerWtrService.isDateConflicting(model.getStartedAt())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("startedAt", "La date sélectionnée est une RTT employeur."));
		}

		if (employerWtrService.isDateConflicting(model.getEndedAt())) {
			return ResponseEntity
				.badRequest()
				.body(Map.of("endedAt", "La date sélectionnée est une RTT employeur."));
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
				.body(Map.of("reason", "Veuillez spécifier un motif pour votre demande de congés sans solde."));
		}

		absenceRequest
			.setType(model.getType())
			.setStatus(AbsenceRequestStatus.INITIAL)
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

		return ResponseEntity.ok(Map.of("message", "La demande d'absence a été modifiée."));
	}

	@PatchMapping("/{id}/approve")
	@Secured("MANAGER")
	public ResponseEntity<Map<String, String>> approveAbsenceRequest(final Authentication authentication, @PathVariable final long id) {
		final AbsenceRequest absenceRequest = absenceRequestService.find(id);

		// Verify that this absence request exists
		if (absenceRequest == null) {
			return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message", "Cette demande d'absence n'existe pas ou plus."));
		}

		final User user = absenceRequest.getUser();
		final Manager manager = (Manager) userService.loadUserByUsername(authentication.getName());

		// Verify that the absence request is owned by an employee of this manager
		// and that they're in the same service
		if (!manager.getEmployees().contains(user) || manager.getService() != user.getService()) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "Vous n'avez pas la permission de valider cette demande d'absence."));
		}

		// Verify that the absence request is in pending state
		if (absenceRequest.getStatus() != AbsenceRequestStatus.PENDING) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "Cette demande d'absence n'est pas en attente de validation."));
		}

		absenceRequest.setStatus(AbsenceRequestStatus.APPROVED);

		absenceRequestService.save(absenceRequest);

		return ResponseEntity.ok(Map.of("message", "La demande d'absence a été validée."));
	}

	@PatchMapping("/{id}/reject")
	@Secured("MANAGER")
	public ResponseEntity<Map<String, String>> rejectAbsenceRequest(final Authentication authentication, @PathVariable final long id) {
		final AbsenceRequest absenceRequest = absenceRequestService.find(id);

		// Verify that this absence request exists
		if (absenceRequest == null) {
			return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message", "Cette demande d'absence n'existe pas ou plus."));
		}

		final User user = absenceRequest.getUser();
		final Manager manager = (Manager) userService.loadUserByUsername(authentication.getName());

		// Verify that the absence request is owned by an employee of this manager
		// and that they're in the same service
		if (!manager.getEmployees().contains(user) || manager.getService() != user.getService()) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "Vous n'avez pas la permission de refuser cette demande d'absence."));
		}

		// Verify that the absence request is in pending state
		if (absenceRequest.getStatus() != AbsenceRequestStatus.PENDING) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "Cette demande d'absence n'est pas en attente de validation."));
		}

		absenceRequest.setStatus(AbsenceRequestStatus.REJECTED);

		absenceRequestService.save(absenceRequest);

		return ResponseEntity.ok(Map.of("message", "La demande d'absence a été refusée."));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteAbsenceRequest(final Authentication authentication, @PathVariable final long id) {
		final AbsenceRequest absenceRequest = absenceRequestService.find(id);

		// Verify that this absence request exists
		if (absenceRequest == null) {
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