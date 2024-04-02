package com.absencia.diginamic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

import org.hibernate.annotations.NamedQuery;

@Entity
@NamedQuery(
	name="PublicHoliday.findByYear",
	query="""
		SELECT ph
		FROM PublicHoliday ph
		WHERE YEAR(ph.date) = :year
	"""
)
@NamedQuery(
		name="PublicHoliday.findByMonthAndYear",
		query="""
		SELECT ph
		FROM PublicHoliday ph
		WHERE YEAR(ph.date) = :year
		AND MONTH(ph.date) = :month
	"""
)
public class PublicHoliday {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;

	@Column(length=255)
	private String label;

	@JsonIgnore
	private boolean worked;

	public PublicHoliday() {
		worked = false;
	}

	@Override
	public boolean equals(final Object publicHoliday) {
		if (!(publicHoliday instanceof PublicHoliday)) {
			return false;
		}

		return id.equals(((PublicHoliday) publicHoliday).getId());
	}

	public Long getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}

	public PublicHoliday setDate(final LocalDate date) {
		this.date = date;

		return this;
	}

	public String getLabel() {
		return label;
	}

	public PublicHoliday setLabel(final String label) {
		this.label = label;

		return this;
	}

	public boolean isWorked() {
		return worked;
	}

	public PublicHoliday setWorked(final boolean worked) {
		this.worked = worked;

		return this;
	}
}