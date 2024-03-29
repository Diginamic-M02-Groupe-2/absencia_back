package com.absencia.diginamic.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class WeekEndService {
    public boolean isWeekEndDay(final LocalDate startDate){
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
