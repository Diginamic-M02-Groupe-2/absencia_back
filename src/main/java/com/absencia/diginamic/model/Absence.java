package com.absencia.diginamic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Absence {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.ORDINAL)
	private AbsenceType type;

	@Column
	private Date startedAt;

	@Column
	private Date endedAt;

	@Column(nullable=true)
	@JsonIgnore
	private Date deletedAt;

	public Long getId() {
		return id;
	}

	public AbsenceType getType() {
		return type;
	}

	public Absence setType(final AbsenceType type) {
		this.type = type;

		return this;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public Absence setStartedAt(final Date startedAt) {
		this.startedAt = startedAt;

		return this;
	}

	public Date getEndedAt() {
		return endedAt;
	}

	public Absence setEndedAt(final Date endedAt) {
		this.endedAt = endedAt;

		return this;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public Absence setDeletedAt(final Date deletedAt) {
		this.deletedAt = deletedAt;

		return this;
	}
}