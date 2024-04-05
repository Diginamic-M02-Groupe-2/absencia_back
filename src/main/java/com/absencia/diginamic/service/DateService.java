package com.absencia.diginamic.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.stereotype.Service;

@Service
public class DateService {
	/**
	 * @param startDate The date to check
	 */
	public boolean isWeekEndDay(final LocalDate startDate) {
		final DayOfWeek dayOfWeek = startDate.getDayOfWeek();

		return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
	}

	/**
	 * @param month The month to get the days from
	 * @param year The year to get the month from
	 */
	public int getDaysInMonth(int month, int year) {
		YearMonth yearMonthObject = YearMonth.of(year, month);
		return yearMonthObject.lengthOfMonth();
	}
}