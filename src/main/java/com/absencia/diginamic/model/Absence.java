package com.absencia.diginamic.model;

import com.absencia.diginamic.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Absence {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(View.AbsenceRequest.class)
	private Long id;

	@Enumerated(EnumType.ORDINAL)
	@JsonView(View.AbsenceRequest.class)
	private AbsenceType type;

	@JsonView(View.AbsenceRequest.class)
	private Date startedAt;

	@JsonView(View.AbsenceRequest.class)
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