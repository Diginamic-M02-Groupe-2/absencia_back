package com.absencia.diginamic.model;

import com.absencia.diginamic.entity.AbsenceType;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

public class PostAbsenceRequestModel {
	@NotNull(message="Veuillez sélectionner une date de début.")
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
	private LocalDate startedAt;

	@NotNull(message="Veuillez sélectionner une date de fin.")
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
	private LocalDate endedAt;

	@NotNull(message="Veuillez sélectionner un type d'absence.")
	private AbsenceType type;

	@Length(max=255, message="Ce champ ne doit pas dépasser 255 caractères.")
	private String reason;

	public LocalDate getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(final LocalDate startedAt) {
		this.startedAt = startedAt;
	}

	public LocalDate getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(final LocalDate endedAt) {
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