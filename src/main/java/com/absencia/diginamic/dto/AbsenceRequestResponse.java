package com.absencia.diginamic.dto;

import com.absencia.diginamic.model.Absence;
import com.absencia.diginamic.model.AbsenceRequestStatus;

public class AbsenceRequestResponse {

    private Absence absence;
    private AbsenceRequestStatus status;
    private String reason;

    public AbsenceRequestResponse(Absence absence, AbsenceRequestStatus status, String reason) {
        this.absence = absence;
        this.status = status;
        this.reason = reason;
    }

    public Absence getAbsence() {
        return absence;
    }

    public void setAbsence(Absence absence) {
        this.absence = absence;
    }

    public AbsenceRequestStatus getStatus() {
        return status;
    }

    public void setStatus(AbsenceRequestStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}