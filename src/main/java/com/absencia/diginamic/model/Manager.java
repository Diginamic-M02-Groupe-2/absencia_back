package com.absencia.diginamic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.Set;

@Entity
public class Manager extends User {
	@OneToMany(mappedBy="manager")
	private Set<Employee> employees;

	public Manager() {
		super();

		setRole(Role.MANAGER);
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public Manager addEmployee(final Employee employee) {
		employees.add(employee);

		return this;
	}

	public Manager removeEmployee(final Employee employee) {
		employees.remove(employee);

		return this;
	}
}