package com.absencia.diginamic.entity;

import com.absencia.diginamic.entity.User.User;
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
	name="AbsenceRequest.countByUserAndTypeAndStatusAndDeletedAtIsNull",
	query="""
		SELECT COUNT(1)
		FROM AbsenceRequest ar
		WHERE ar.deletedAt IS NULL
		AND ar.user = :user
		AND ar.type = :type
		AND ar.status = :status
	"""
)
@NamedQuery(
	name="AbsenceRequest.countByIdAndUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndDeletedAtIsNull",
	query="""
		SELECT COUNT(1)
		FROM AbsenceRequest ar
		WHERE ar.deletedAt IS NULL
		AND ar.id != :id
		AND ar.user = :user
		AND ar.startedAt <= :endedAt
		AND ar.endedAt >= :startedAt
	"""
)
public class AbsenceRequest {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(nullable=false)
	@JsonIgnore
	private User user;

	@Enumerated(EnumType.ORDINAL)
	private AbsenceType type;

	@Enumerated(EnumType.ORDINAL)
	private AbsenceRequestStatus status;

	private Date startedAt;

	private Date endedAt;

	@Column(length=255, nullable=true)
	private String reason;

	@Column(nullable=true)
	@JsonIgnore
	private Date deletedAt;

	public AbsenceRequest() {
		status = AbsenceRequestStatus.INITIAL;
	}

	@Override
	public boolean equals(final Object absenceRequest) {
		if (!(absenceRequest instanceof AbsenceRequest)) {
			return false;
		}

		return id.equals(((AbsenceRequest) absenceRequest).getId());
	}

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

	public AbsenceType getType() {
		return type;
	}

	public AbsenceRequest setType(final AbsenceType type) {
		this.type = type;

		return this;
	}

	public AbsenceRequestStatus getStatus() {
		return status;
	}

	public AbsenceRequest setStatus(final AbsenceRequestStatus status) {
		this.status = status;

		return this;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public AbsenceRequest setStartedAt(final Date startedAt) {
		this.startedAt = startedAt;

		return this;
	}

	public Date getEndedAt() {
		return endedAt;
	}

	public AbsenceRequest setEndedAt(final Date endedAt) {
		this.endedAt = endedAt;

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