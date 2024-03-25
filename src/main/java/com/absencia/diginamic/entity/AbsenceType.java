package com.absencia.diginamic.entity;

public enum AbsenceType {
	PUBLIC_HOLIDAY(0), // Jour férié
	PAID_LEAVE(1), // Congé payé
	UNPAID_LEAVE(2), // Congé sans solde
	EMPLOYEE_WTR(3), // RTT employé
	EMPLOYER_WTR(4); // RTT employeur

	public final int value;

	private AbsenceType(final int value) {
		this.value = value;
	}
}