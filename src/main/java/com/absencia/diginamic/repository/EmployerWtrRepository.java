package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.EmployerWtr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerWtrRepository extends JpaRepository<EmployerWtr, Long> {
	List<EmployerWtr> findByYear(final int year);
	long countApproved();
}