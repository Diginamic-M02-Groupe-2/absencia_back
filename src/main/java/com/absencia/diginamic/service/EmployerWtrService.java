package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.repository.EmployerWtrRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class EmployerWtrService {
	public static final long MAX_EMPLOYER_WTR = 5;

	private final EmployerWtrRepository employerWtrRepository;

	public EmployerWtrService(final EmployerWtrRepository employerWtrRepository) {
		this.employerWtrRepository = employerWtrRepository;
	}

	/**
	 * @param id The ID of the employer WTR to get
	 */
	public EmployerWtr findOneByIdAndDeletedAtIsNull(final long id) {
		return employerWtrRepository.findOneByIdAndDeletedAtIsNull(id);
	}

	/**
	 * @param employerWtr The employer WTR to save
	 */
	public void save(@NonNull final EmployerWtr employerWtr) {
		employerWtrRepository.save(employerWtr);
	}

	/**
	 * @param employerWtr The employer WTR to delete
	 */
	public void delete(final EmployerWtr employerWtr) {
		employerWtr.setDeletedAt(LocalDate.now());

		save(employerWtr);
	}

	/**
	 * @param year The year to filter the employer WTR by
	 */
	public List<EmployerWtr> findApprovedByYear(final int year) {
		return employerWtrRepository.findApprovedByYear(year);
	}

	public List<EmployerWtr> findInitial() {
		return employerWtrRepository.findInitial();
	}

	public long countRemainingEmployerWtr() {
		final long count = employerWtrRepository.countApproved();

		return MAX_EMPLOYER_WTR - count;
	}

	/**
	 * @param date The date to check for conflicting employer WTR
	 */
	public boolean isDateConflicting(final LocalDate date) {
		return employerWtrRepository.existsByDate(date);
	}

	/**
	 * @param id The ID of the employer WTR to check date conflict with
	 * @param date The date to check for conflicting employer WTR
	 */
	public boolean isDateConflictingWithOther(final Long id, final LocalDate date) {
		return employerWtrRepository.isDateConflictingWithOther(id, date);
	}

	/**
	 * @param year The year to build the date object
	 * @param employees The List of the employees to get the data from
	 * @return
	 */
	public List<EmployerWtr> findApprovedByYearAndEmployees(int year, List<User> employees) {
		List<Long> employeeIds = employees.stream()
				.map(User::getId)
				.collect(Collectors.toList());
		return employerWtrRepository.findApprovedByYearAndEmployees(year, employeeIds);
	}
}