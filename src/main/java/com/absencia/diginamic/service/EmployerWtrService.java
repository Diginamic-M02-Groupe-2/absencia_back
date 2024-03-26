package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.repository.EmployerWtrRepository;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class EmployerWtrService {
	public static final long MAX_EMPLOYER_WTR = 5;

	private EmployerWtrRepository employerWtrRepository;

	@Autowired
	public EmployerWtrService(final EmployerWtrRepository employerWtrRepository) {
		this.employerWtrRepository = employerWtrRepository;
	}

	public void save(@NonNull final EmployerWtr employerWtr) {
		employerWtrRepository.save(employerWtr);
	}

	public void delete(final EmployerWtr employerWtr) {
		employerWtr.setDeletedAt(LocalDate.now());

		save(employerWtr);
	}

	public long countRemainingEmployerWtr() {
		final long count = employerWtrRepository.countApproved();

		return MAX_EMPLOYER_WTR - count;
	}
}