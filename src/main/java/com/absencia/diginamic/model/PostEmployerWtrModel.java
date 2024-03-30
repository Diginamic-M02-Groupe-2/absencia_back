package com.absencia.diginamic.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

public class PostEmployerWtrModel {
	@NotNull(message="Ce champ est requis.")
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
	private LocalDate date;

	@NotBlank(message="Ce champ est requis")
	@Length(max=255, message="Ce champ ne doit pas dépasser 255 caractères.")
	private String label;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}
}