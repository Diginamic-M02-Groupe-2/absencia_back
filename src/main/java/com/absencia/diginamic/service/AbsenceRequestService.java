package com.absencia.diginamic.service;

import com.absencia.diginamic.model.AbsenceRequest;

import java.util.List;

public interface AbsenceRequestService {
    List<AbsenceRequest> getAbsencesByUserId(Long userId);

}
