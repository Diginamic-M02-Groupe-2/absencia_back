package com.absencia.diginamic.entity.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name="user_manager")
public class Manager extends User {
	@OneToMany(mappedBy="manager", fetch=FetchType.EAGER)
	@JsonIgnore
	private List<Employee> employees;

	public List<Employee> getEmployees() {
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