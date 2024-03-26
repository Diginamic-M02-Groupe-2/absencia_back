package com.absencia.diginamic.entity;

public enum EmployerWtrStatus {
	INITIAL(0),
	APPROVED(1);

	public final int value;

	private EmployerWtrStatus(final int value) {
		this.value = value;
	}
}