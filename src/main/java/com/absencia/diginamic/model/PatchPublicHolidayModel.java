package com.absencia.diginamic.model;

import jakarta.validation.constraints.NotNull;

public class PatchPublicHolidayModel {
	@NotNull(message="Ce champ est requis.")
	private boolean worked;

	public boolean isWorked() {
		return worked;
	}

	public void setWorked(final boolean worked) {
		this.worked = worked;
	}
}