package com.absencia.diginamic.controller;

import com.absencia.diginamic.dto.AbsenceRequestResponse;
import com.absencia.diginamic.model.Absence;
import com.absencia.diginamic.model.AbsenceRequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.absencia.diginamic.model.AbsenceRequest;
import com.absencia.diginamic.service.AbsenceRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AbsenceRequestController {

    private static final Logger logger = LoggerFactory.getLogger(AbsenceRequestController.class);

    @Autowired
    private AbsenceRequestService absenceRequestService;

    @GetMapping("/absences-requests/{userId}")
    public ResponseEntity<List<AbsenceRequestResponse>> getAbsencesByUserId(@PathVariable Long userId) {
        if (!absenceRequestService.userExists(userId)) {
            logger.error("Utilisateur non trouvé avec l'ID : {}", userId);
            return ResponseEntity.notFound().build();
        }

        List<AbsenceRequestResponse> absencesRequests  = absenceRequestService.getAbsencesByUserId(userId);

        return ResponseEntity.ok(absencesRequests);
    }
}
