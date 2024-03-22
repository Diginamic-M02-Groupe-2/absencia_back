package com.absencia.diginamic.service;

import com.absencia.diginamic.model.AbsenceRequest;
import com.absencia.diginamic.repository.AbsenceRequestRepository;
import com.absencia.diginamic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "absenceRequestService")
public class AbsenceRequestServiceImpl implements AbsenceRequestService {

    @Autowired
    private AbsenceRequestRepository absenceRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<AbsenceRequest> getAbsencesByUserId(Long userId) {
        final List<AbsenceRequest> absenceRequests = absenceRequestRepository.findByUserId(userId);

        return absenceRequests;
    }
}
