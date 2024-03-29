package com.absencia.diginamic.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.AbsenceType;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.service.AbsenceRequestService;
import com.absencia.diginamic.entity.AbsenceRequestStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class NightTask {
	// private static final Logger logger = LoggerFactory.getLogger(NightTask.class);

	private final AbsenceRequestService absenceRequestService;

	@Autowired
	public NightTask(AbsenceRequestService absenceRequestService) {
		this.absenceRequestService = absenceRequestService;
	}

	@Scheduled(cron="0 0 0 * * *")
	public void run() {
		System.out.println("From NightTask");

		/* logger.info("Scheduled task nightBatch started.");
		//Assure que les demandes soient traitées dans l'odre chronologique des dates de début
		initialRequest.sort(Comparator.comparing(request -> request.getStartedAt()));

		for (AbsenceRequest request : initialRequest) {
			logger.info("Processing AbsenceRequest ID: {}", request.getId());

			if (request.getType() == AbsenceType.PAID_LEAVE || request.getType() == AbsenceType.UNPAID_LEAVE || request.getType() == AbsenceType.EMPLOYEE_WTR) {
				//S'il reste assez de jours pour le type d'absence demandé, la demande passe au statut  EN_ATTENTE_VALIDATION
				handleCommonAbsences(request);
			} else if (request.getType() == AbsenceType.EMPLOYEE_WTR) {
				//Si le type de l'absence est un RTT employé ça passe automatiquement en approuvé
				request.setStatus(AbsenceRequestStatus.APPROVED);
			}
		}
		logger.info("Scheduled task nightBatch completed."); */

	}

	/* private void handleCommonAbsences(AbsenceRequest request) {
		if (hasEnoughDays(request)) { 
			request.setStatus(AbsenceRequestStatus.PENDING); // Si la condition est remplie, ça signifie qu'il reste des jours disponibles, donc la demande est mise à l'état "EN_ATTENTE_VALIDATION" 
		} else {
			request.setStatus(AbsenceRequestStatus.REJECTED); //Si la condition précédente n'est pas bonne, ça signifie qu'il ne reste plus de jours disponibles pour cette demande, donc la demande est mise à l'état "REJECTED"
		}
	}

	private boolean hasEnoughDays(AbsenceRequest request) {
		User user = request.getUser();
		AbsenceType type = request.getType();
		LocalDate startDate = request.getStartedAt();
		LocalDate endDate = request.getEndedAt();

		// obtenir le nombre de jours restants pour l'utilisateur et le type d'absence
		long remainingDays = 0;
		if (type == AbsenceType.PAID_LEAVE) {
			remainingDays = absenceRequestService.countRemainingPaidLeaves(user);
		} else if (type == AbsenceType.EMPLOYEE_WTR) {
			remainingDays = absenceRequestService.countRemainingEmployeeWtr(user);
		}

		// Calculer la durée de la demande d'absence
		long requestedDays = ChronoUnit.DAYS.between(startDate, endDate) + 1; // Ajouter 1 car on inclut également le dernier jour

		// Vérifie si l'utilisateur a suffisamment de jours disponibles
		return remainingDays >= requestedDays;
	} */
}