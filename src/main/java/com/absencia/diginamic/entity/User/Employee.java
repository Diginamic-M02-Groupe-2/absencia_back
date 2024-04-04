package com.absencia.diginamic.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="user_employee")
public class Employee extends User {
	@ManyToOne
	@JoinColumn(nullable=false)
	private Manager manager;

	public Manager getManager() {
		return manager;
	}

	public Employee setManager(final Manager manager) {
		this.manager = manager;

		return this;
	}
}