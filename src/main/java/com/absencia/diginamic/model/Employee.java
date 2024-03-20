package com.absencia.diginamic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Employee extends User {
	@ManyToOne
	@JoinColumn(nullable=false)
	private Manager manager;

	public Employee() {
		super();

		setRole(Role.EMPLOYEE);
	}

	public Manager getManager() {
		return manager;
	}

	public Employee setManager(final Manager manager) {
		this.manager = manager;

		return this;
	}
}