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

	public PublicHoliday findOneById(final long id) {
		return publicHolidayRepository.findOneById(id);
	}

	public void save(@NonNull final PublicHoliday publicHoliday) {
		publicHolidayRepository.save(publicHoliday);
	}

	public List<PublicHoliday> findByYear(final int year) {
		return publicHolidayRepository.findByYear(year);
	}

	public boolean isDateConflicting(LocalDate date) {
		return publicHolidayRepository.existsByDate(date);
	}

	public List<PublicHoliday> findByMonthAndYear(int month, int year) {
		return publicHolidayRepository.findByMonthAndYear(month, year);
	}

}