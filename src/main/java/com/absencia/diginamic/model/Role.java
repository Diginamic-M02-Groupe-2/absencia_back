package com.absencia.diginamic.model;

public enum Role {
	ADMINISTRATOR(0),
	EMPLOYEE(1),
	MANAGER(2);

	public final int value;

	private Role(final int value) {
		this.value = value;
	}
}