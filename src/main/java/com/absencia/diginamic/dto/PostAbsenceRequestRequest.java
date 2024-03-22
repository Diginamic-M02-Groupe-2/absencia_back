package com.absencia.diginamic.dto;

import com.absencia.diginamic.model.AbsenceType;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

public class PostAbsenceRequestRequest {
	@NotNull(message="Veuillez sélectionner une date de début.")
	public Date startedAt;

	@NotNull(message="Veuillez sélectionner une date de fin.")
	public Date endedAt;

	@NotNull(message="Veuillez sélectionner un type d'absence.")
	public AbsenceType type;

	@Length(max=255, message="Ce champ ne doit pas dépasser 255 caractères.")
	public String reason;
}