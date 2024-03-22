package com.absencia.diginamic.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.absencia.diginamic.model.AbsenceRequest;
import com.absencia.diginamic.repository.AbsenceRequestRepository;

import jakarta.transaction.Transactional;

public class AbsenceRequestServiceImpl implements AbsenceRequestService {

    @Autowired
    AbsenceRequestRepository absenceRequestRepository;

    @Override
    @Transactional
    public AbsenceRequest delete(Long id) {
        AbsenceRequest absenceRequest = absenceRequestRepository.findById(id).orElse(null);
        if (absenceRequest != null) {
            absenceRequestRepository.delete(absenceRequest);
            return absenceRequest;
        }
        return null;
    }
    // public void AbsenceRequest delete(AbsenceRequest absenceRequest) {

    // // absenceRequest.setDeletedAt();

    // }

}