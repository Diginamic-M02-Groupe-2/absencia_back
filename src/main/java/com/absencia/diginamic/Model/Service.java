package com.absencia.diginamic.Model;

public enum Service {
	MANAGEMENT,
	DEVELOPMENT,
	DESIGN,
	MARKETING,
	SALES;

	public final int value;

	private Service() {
		this.value = ordinal();
	}
}