package com.absencia.diginamic.service;

import com.absencia.diginamic.dto.AbsenceRequestResponse;
import com.absencia.diginamic.model.AbsenceRequest;
import com.absencia.diginamic.repository.AbsenceRequestRepository;
import com.absencia.diginamic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service(value = "absenceRequestService")
public class AbsenceRequestImpl implements AbsenceRequestService {

    @Autowired
    private AbsenceRequestRepository absenceRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<AbsenceRequestResponse> getAbsencesByUserId(Long userId) {
        List<AbsenceRequest> absencesRequests = absenceRequestRepository.findByUserId(userId);
        List<AbsenceRequestResponse> absencesRequestPayload = absencesRequests.stream()
                .map(this::mapToAbsencesRequestPayload)
                .collect(Collectors.toList());
        return absencesRequestPayload;
    }

    @Override
    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }

    private AbsenceRequestResponse mapToAbsencesRequestPayload(AbsenceRequest absenceRequest) {
        return new AbsenceRequestResponse(
                absenceRequest.getId(),
                absenceRequest.getAbsence(),
                absenceRequest.getStatus(),
                absenceRequest.getReason()
        );
    }
}
