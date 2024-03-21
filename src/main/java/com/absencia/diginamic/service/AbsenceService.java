package com.absencia.diginamic.service;

import com.absencia.diginamic.model.Absence;
import com.absencia.diginamic.repository.AbsenceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbsenceService {
	@Autowired
	private AbsenceRepository absenceRepository;

	public void save(final Absence absence) {
		absenceRepository.save(absence);
	}

	public Absence find(final Long id) {
		return absenceRepository.findOneById(id);
	}
}