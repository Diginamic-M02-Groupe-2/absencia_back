package com.absencia.diginamic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

import java.util.Date;

@Entity
@NamedQuery(
	name="EmployerWtr.countApproved",
	query="""
		SELECT COUNT(1)
		FROM EmployerWtr ew
		WHERE ew.deletedAt IS NULL
		AND ew.status = EmployerWtrStatus.APPROVED
	"""
)
public class EmployerWtr {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.ORDINAL)
	private EmployerWtrStatus status;

	private Date date;

	@Column(length=255)
	private String label;

	@Column(nullable=true)
	@JsonIgnore
	private Date deletedAt;

	public EmployerWtr() {
		status = EmployerWtrStatus.INITIAL;
	}

	@Override
	public boolean equals(final Object employerWtr) {
		if (!(employerWtr instanceof EmployerWtr)) {
			return false;
		}

		return id.equals(((EmployerWtr) employerWtr).getId());
	}

	public Long getId() {
		return id;
	}

	public EmployerWtrStatus getStatus() {
		return status;
	}

	public EmployerWtr setStatus(final EmployerWtrStatus status) {
		this.status = status;

		return this;
	}

	public Date getDate() {
		return date;
	}

	public EmployerWtr setDate(final Date date) {
		this.date = date;

		return this;
	}

	public String getLabel() {
		return label;
	}

	public EmployerWtr setLabel(final String label) {
		this.label = label;

		return this;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public EmployerWtr setDeletedAt(final Date deletedAt) {
		this.deletedAt = deletedAt;

		return this;
	}
}