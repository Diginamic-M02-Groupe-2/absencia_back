package com.absencia.diginamic.service;

import com.absencia.diginamic.model.AbsenceRequest;
import com.absencia.diginamic.repository.AbsenceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "absenceRequestService")
public class AbsenceRequestServiceImpl implements AbsenceRequestService {

    @Autowired
    private AbsenceRequestRepository absenceRequestRepository;

    @Override
    public List<AbsenceRequest> getAbsencesByUserId(Long userId) {
        final List<AbsenceRequest> absenceRequests = absenceRequestRepository.findByUserId(userId);

        return absenceRequests;
    }
}
