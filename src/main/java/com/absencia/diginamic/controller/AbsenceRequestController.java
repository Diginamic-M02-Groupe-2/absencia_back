package com.absencia.diginamic.controller;

import com.absencia.diginamic.dto.ErrorResponse;
import com.absencia.diginamic.model.AbsenceRequest;
import com.absencia.diginamic.service.UserService;
import com.absencia.diginamic.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.absencia.diginamic.service.AbsenceRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AbsenceRequestController {

    private static final Logger logger = LoggerFactory.getLogger(AbsenceRequestController.class);

    @Autowired
    private AbsenceRequestService absenceRequestService;

    @Autowired
    private UserService userService;

    @GetMapping("/absence-requests/{userId}")
    @JsonView(View.AbsenceRequest.class)
    public ResponseEntity<List<?>> getAbsencesByUserId(@PathVariable Long userId) {
        if (!userService.userExists(userId)) {
            logger.error("Utilisateur non trouvé avec l'ID : {}", userId);
            ErrorResponse errorResponse = new ErrorResponse("L'Utilisateur fourni n'existe pas");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonList(errorResponse));
        }

        final List<AbsenceRequest> absencesRequests  = absenceRequestService.getAbsencesByUserId(userId);

        return ResponseEntity.ok(absencesRequests);
    }
}
