package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.PublicHoliday;
import com.absencia.diginamic.repository.PublicHolidayRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class PublicHolidayService {
	private PublicHolidayRepository publicHolidayRepository;

	@Autowired
	public PublicHolidayService(final PublicHolidayRepository publicHolidayRepository) {
		this.publicHolidayRepository = publicHolidayRepository;
	}

	public void save(@NonNull final PublicHoliday publicHoliday) {
		publicHolidayRepository.save(publicHoliday);
	}

	public List<PublicHoliday> findByYear(final int year) {
		return publicHolidayRepository.findByYear(year);
	}
}