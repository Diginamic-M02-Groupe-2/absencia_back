package com.absencia.diginamic.model;

import com.absencia.diginamic.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

@Entity
public class AbsenceRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(View.AbsenceRequest.class)
	private Long id;

	@ManyToOne
	@JoinColumn(nullable=false)
	private User user;

	@ManyToOne
	@JoinColumn(nullable=false)
	@JsonView(View.AbsenceRequest.class)
	private Absence absence;

	@Enumerated(EnumType.ORDINAL)
	@JsonView(View.AbsenceRequest.class)
	private AbsenceRequestStatus status;

	@Column(length=255, nullable=true)
	@JsonView(View.AbsenceRequest.class)
	private String reason;

	@Column(nullable=true)
	@JsonIgnore
	private Date deletedAt;

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public AbsenceRequest setUser(final User user) {
		this.user = user;

		return this;
	}

	public Absence getAbsence() {
		return absence;
	}

	public AbsenceRequest setAbsence(final Absence absence) {
		this.absence = absence;

		return this;
	}

	public AbsenceRequestStatus getStatus() {
		return status;
	}

	public AbsenceRequest setStatus(final AbsenceRequestStatus status) {
		this.status = status;

		return this;
	}

	public String getReason() {
		return reason;
	}

	public AbsenceRequest setReason(final String reason) {
		this.reason = reason;

		return this;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public AbsenceRequest setDeletedAt(final Date deletedAt) {
		this.deletedAt = deletedAt;

		return this;
	}
}