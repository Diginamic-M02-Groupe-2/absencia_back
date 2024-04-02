package com.absencia.diginamic.entity;

import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.view.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

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

import java.time.LocalDate;

@Entity
@NamedQuery(
	name="AbsenceRequest.findInitial",
	query="""
		SELECT ar
		FROM AbsenceRequest ar
		WHERE ar.deletedAt IS NULL
		AND ar.status = AbsenceRequestStatus.INITIAL
		ORDER BY ar.startedAt ASC
	"""
)
@NamedQuery(
	name="AbsenceRequest.countRemaining",
	query="""
		SELECT COUNT(1)
		FROM AbsenceRequest ar
		WHERE ar.deletedAt IS NULL
		AND ar.user = :user
		AND ar.type = :type
		AND ar.status = AbsenceRequestStatus.APPROVED
	"""
)
@NamedQuery(
	name="AbsenceRequest.isOverlapping",
	query="""
		SELECT CASE WHEN COUNT(ar) > 0 THEN true ELSE false END
		FROM AbsenceRequest ar
		WHERE ar.deletedAt IS NULL
		AND (:id IS NULL OR ar.id != :id)
		AND ar.user = :user
		AND ar.startedAt < :endedAt
		AND ar.endedAt > :startedAt
	"""
)
@NamedQuery(
		name = "AbsenceRequest.findByMonthYearAndService",
		query = """
        SELECT ar
        FROM AbsenceRequest ar
        JOIN ar.user u
        WHERE FUNCTION('YEAR', ar.startedAt) = :year
        AND FUNCTION('MONTH', ar.startedAt) = :month
        AND u.service = :service
        AND ar.deletedAt IS NULL
    """
)
public class AbsenceRequest {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonView(View.AbsenceRequest.GetEmployeeAbsenceRequests.class)
	private Long id;

	@ManyToOne
	@JoinColumn(nullable=false)
	@JsonIgnore
	private User user;

	@Enumerated(EnumType.ORDINAL)
	@JsonView(View.AbsenceRequest.GetEmployeeAbsenceRequests.class)
	private AbsenceType type;

	@Enumerated(EnumType.ORDINAL)
	@JsonView(View.AbsenceRequest.GetEmployeeAbsenceRequests.class)
	private AbsenceRequestStatus status;

	@JsonView(View.AbsenceRequest.GetEmployeeAbsenceRequests.class)
	private LocalDate startedAt;

	@JsonView(View.AbsenceRequest.GetEmployeeAbsenceRequests.class)
	private LocalDate endedAt;

	@Column(length=255, nullable=true)
	@JsonView(View.AbsenceRequest.GetEmployeeAbsenceRequests.class)
	private String reason;

	@Column(nullable=true)
	@JsonIgnore
	private LocalDate deletedAt;

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

	public LocalDate getStartedAt() {
		return startedAt;
	}

	public AbsenceRequest setStartedAt(final LocalDate startedAt) {
		this.startedAt = startedAt;

		return this;
	}

	public LocalDate getEndedAt() {
		return endedAt;
	}

	public AbsenceRequest setEndedAt(final LocalDate endedAt) {
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

	public LocalDate getDeletedAt() {
		return deletedAt;
	}

	public AbsenceRequest setDeletedAt(final LocalDate deletedAt) {
		this.deletedAt = deletedAt;

		return this;
	}
}