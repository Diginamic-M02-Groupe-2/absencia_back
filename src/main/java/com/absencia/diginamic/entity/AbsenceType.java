package com.absencia.diginamic.entity;

public enum AbsenceType {
	PAID_LEAVE(0), // Congé payé
	UNPAID_LEAVE(1), // Congé sans solde
	EMPLOYEE_WTR(2); // RTT employé

	public final int value;

	private AbsenceType(final int value) {
		this.value = value;
	}
}