package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.Absence;
import com.absencia.diginamic.repository.AbsenceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbsenceService {
	private AbsenceRepository absenceRepository;

	@Autowired
	public AbsenceService(final AbsenceRepository absenceRepository) {
		this.absenceRepository = absenceRepository;
	}

	public void save(final Absence absence) {
		absenceRepository.save(absence);
	}

	public Absence find(final Long id) {
		return absenceRepository.findOneById(id);
	}
}