package com.absencia.diginamic.entity;

public enum Service {
	MANAGEMENT(0),
	DEVELOPMENT(1),
	DESIGN(2),
	MARKETING(3),
	SALES(4);

	public final int value;

	private Service(final int value) {
		this.value = value;
	}
}