package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.PublicHoliday;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {
	PublicHoliday findOneById(final long id);
	List<PublicHoliday> findByYear(final int year);
	boolean existsByDate(LocalDate date);
}