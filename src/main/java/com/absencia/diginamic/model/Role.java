package com.absencia.diginamic.model;

public enum Role {
	ADMINISTRATOR,
	EMPLOYEE,
	MANAGER;

	public final int value;

	private Role() {
		this.value = ordinal();
	}
}