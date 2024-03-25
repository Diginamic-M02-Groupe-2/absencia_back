package com.absencia.diginamic.entity;

public enum AbsenceRequestStatus {
	INITIAL(0),
	PENDING(1),
	APPROVED(2),
	REJECTED(3);

	public final int value;

	private AbsenceRequestStatus(final int value) {
		this.value = value;
	}
}