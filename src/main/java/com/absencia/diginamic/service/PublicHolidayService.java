package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.repository.PublicHolidayRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class PublicHolidayService {
	private final PublicHolidayRepository publicHolidayRepository;

	public PublicHolidayService(final PublicHolidayRepository publicHolidayRepository) {
		this.publicHolidayRepository = publicHolidayRepository;
	}

	/**
	 * @param publicHoliday The public holiday to delete
	 */
	public void save(@NonNull final PublicHoliday publicHoliday) {
		publicHolidayRepository.save(publicHoliday);
	}

	/**
	 * @param id The ID of the public holiday to get
	 */
	public PublicHoliday findOneById(final long id) {
		return publicHolidayRepository.findOneById(id);
	}

	/**
	 * @param year The year to filter the public holidays by
	 */
	public List<PublicHoliday> findByYear(final int year) {
		return publicHolidayRepository.findByYear(year);
	}

	/**
	 * @param date The date to check for conflicting public holidays
	 */
	public boolean isDateConflicting(final LocalDate date) {
		return publicHolidayRepository.existsByDate(date);
	}

	/**
	 * @param month The month to filter the public holidays by
	 * @param year The year to filter the public holidays by
	 */
	public List<PublicHoliday> findByMonthAndYear(final int month, final int year) {
		return publicHolidayRepository.findByMonthAndYear(month, year);
	}

	public void clearTable() {
		publicHolidayRepository.clearTable();
	}
}