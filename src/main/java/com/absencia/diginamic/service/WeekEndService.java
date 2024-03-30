package com.absencia.diginamic.service;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class WeekEndService {
	public boolean isWeekEndDay(final LocalDate startDate) {
		final DayOfWeek dayOfWeek = startDate.getDayOfWeek();

		return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
	}
}