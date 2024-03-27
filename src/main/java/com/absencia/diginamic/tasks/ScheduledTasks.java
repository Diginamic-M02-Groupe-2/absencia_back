package com.absencia.diginamic.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.AbsenceType;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.service.AbsenceRequestService;
import com.absencia.diginamic.entity.AbsenceRequestStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ScheduledTasks {

    private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    List<AbsenceRequest> initialRequest = new ArrayList<>();

    @Autowired
    private AbsenceRequestService absenceRequestService;

    @Scheduled(cron = "0 0 0 * * *")
    private void nightBatch() {
        logger.info("Scheduled task nightBatch started.");
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
        logger.info("Scheduled task nightBatch completed.");

    }

    private void handleCommonAbsences(AbsenceRequest request) {
        if (nbDayLeft(request)) { //vérifie si le nombre de jours restants pour la demande est supérieur à zéro. 
            request.setStatus(AbsenceRequestStatus.PENDING); // Si la condition est remplie, ça signifie qu'il reste des jours disponibles, donc la demande est mise à l'état "EN_ATTENTE_VALIDATION" 
        } else {
            request.setStatus(AbsenceRequestStatus.REJECTED); //Si la condition précédente n'est pas bonne, ça signifie qu'il ne reste plus de jours disponibles pour cette demande, donc la demande est mise à l'état "REJECTED"
        }
    }

    private boolean nbDayLeft(AbsenceRequest request) {
        User user = request.getUser();
        AbsenceType type = request.getType();
    
        // obtenir le nombre de jours restants pour l'utilisateur et le type d'absence
        long remainingDays = 0;
        if (type == AbsenceType.PAID_LEAVE) {
            remainingDays = absenceRequestService.countRemainingPaidLeaves(user);
        } else if (type == AbsenceType.EMPLOYEE_WTR) {
            remainingDays = absenceRequestService.countRemainingEmployeeWtr(user);
        }
    
        // Vérifie si l'utilisateur a suffisamment de jours disponibles
        return remainingDays > 0;
    }
    
}