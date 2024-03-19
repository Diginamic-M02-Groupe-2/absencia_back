package com.absencia.diginamic.model;

public enum AbsenceType {
	PUBLIC_HOLIDAY, // Férié
	PAID_LEAVE, // Congé payé
	UNPAID_LEAVE, // Congé sans solde
	TOIL_DAY; // RTT

	public final int value;

	private AbsenceType() {
		this.value = ordinal();
	}
}