package com.absencia.diginamic.service;

import com.absencia.diginamic.dto.AbsenceRequestResponse;

import java.util.List;

public interface AbsenceRequestService {
    List<AbsenceRequestResponse> getAbsencesByUserId(Long userId);

    boolean userExists(Long userId);
}
