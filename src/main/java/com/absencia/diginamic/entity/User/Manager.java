package com.absencia.diginamic.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name="user_manager")
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