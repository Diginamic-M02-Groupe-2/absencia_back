package com.absencia.diginamic.controller;

import com.absencia.diginamic.dto.AbsenceRequestResponse;
import com.absencia.diginamic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.absencia.diginamic.service.AbsenceRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

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
    public ResponseEntity<List<AbsenceRequestResponse>> getAbsencesByUserId(@PathVariable Long userId) {
        if (!userService.userExists(userId)) {
            logger.error("Utilisateur non trouvé avec l'ID : {}", userId);
            return ResponseEntity.notFound().build();
        }

        List<AbsenceRequestResponse> absencesRequests  = absenceRequestService.getAbsencesByUserId(userId);

        return ResponseEntity.ok(absencesRequests);
    }
}
