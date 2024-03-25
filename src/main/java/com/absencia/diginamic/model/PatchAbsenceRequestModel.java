package com.absencia.diginamic.model;

import com.absencia.diginamic.entity.AbsenceType;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

public class PatchAbsenceRequestModel {
	@NotNull(message="Veuillez sélectionner une date de début.")
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
	private Date startedAt;

	@NotNull(message="Veuillez sélectionner une date de fin.")
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
	private Date endedAt;

	@NotNull(message="Veuillez sélectionner un type d'absence.")
	private AbsenceType type;

	@Length(max=255, message="Ce champ ne doit pas dépasser 255 caractères.")
	private String reason;

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(final Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(final Date endedAt) {
		this.endedAt = endedAt;
	}

	public AbsenceType getType() {
		return type;
	}

	public void setType(final AbsenceType type) {
		this.type = type;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(final String reason) {
		this.reason = reason;
	}
}