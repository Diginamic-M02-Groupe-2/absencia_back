package com.absencia.diginamic.repository;

import com.absencia.diginamic.model.Absence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
	Absence findOneById(final Long id);
}