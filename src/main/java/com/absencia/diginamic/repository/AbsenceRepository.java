package com.absencia.diginamic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.absencia.diginamic.entity.Absence;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
	Absence findOneById(final Long id);
}