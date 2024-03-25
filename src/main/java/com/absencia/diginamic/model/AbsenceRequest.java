package com.absencia.diginamic.model;

import com.absencia.diginamic.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;

import java.util.Date;

@Entity
@NamedQuery(
	name="AbsenceRequest.countByUserAndStatusAndTypeAndDeletedAtIsNull",
	query="""
		SELECT COUNT(1)
		FROM AbsenceRequest ar
		LEFT JOIN ar.absence a
		WHERE ar.deletedAt IS NULL
		AND ar.user = :user
		AND ar.status = :status
		AND a.deletedAt IS NULL
		AND a.type = :type
	"""
)
@NamedQuery(
	name="AbsenceRequest.countByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndDeletedAtIsNull",
	query="""
		SELECT COUNT(1)
		FROM AbsenceRequest ar
		LEFT JOIN ar.absence a
		WHERE ar.deletedAt IS NULL
		AND ar.user = :user
		AND a.deletedAt IS NULL
		AND a.startedAt <= :endedAt
		AND a.endedAt >= :startedAt
	"""
)
public class AbsenceRequest {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonView(View.AbsenceRequest.class)
	private Long id;

	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(nullable=false)
	private User user;

	@ManyToOne(cascade=CascadeType.PERSIST)
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

	public Long setId(Long id) {
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