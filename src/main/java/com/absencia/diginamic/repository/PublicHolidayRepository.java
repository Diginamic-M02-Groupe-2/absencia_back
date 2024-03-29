package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.PublicHoliday;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {
	PublicHoliday findOneById(final Long id);
	List<PublicHoliday> findByYear(final int year);
}