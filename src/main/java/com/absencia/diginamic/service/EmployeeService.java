package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.User.Employee;
import com.absencia.diginamic.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements UserDetailsService {
	private EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeService(final EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Employee loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Employee employee = employeeRepository.findOneByEmail(username);

		if (employee == null) {
			throw new UsernameNotFoundException("Invalid credentials.");
		}

		return employee;
	}

	public void save(@NonNull final Employee employee) {
		employeeRepository.save(employee);
	}

	public Employee find(final long id) {
		return employeeRepository.findOneById(id);
	}
}