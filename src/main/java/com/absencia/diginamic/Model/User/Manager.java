package com.absencia.diginamic.Model.User;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Manager extends User {
	@OneToMany(mappedBy="manager")
	private Set<Employee> employees;

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