package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.User.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	Employee findOneById(final long id);
	Employee findOneByEmail(final String email);
}