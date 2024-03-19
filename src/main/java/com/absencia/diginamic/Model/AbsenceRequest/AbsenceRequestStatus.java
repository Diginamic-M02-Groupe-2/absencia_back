package com.absencia.diginamic.Model.AbsenceRequest;

public enum AbsenceRequestStatus {
	INITIAL,
	PENDING,
	APPROVED,
	REJECTED;

	public final int value;

	private AbsenceRequestStatus() {
		this.value = ordinal();
	}
}