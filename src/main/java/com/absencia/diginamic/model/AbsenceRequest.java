package com.absencia.diginamic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AbsenceRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(nullable=false)
	private User user;

	@ManyToOne
	@JoinColumn(nullable=false)
	private Absence absence;

	@Enumerated(EnumType.ORDINAL)
	private AbsenceRequestStatus status;

	@Column(length=255, nullable=true)
	private String reason;

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
}