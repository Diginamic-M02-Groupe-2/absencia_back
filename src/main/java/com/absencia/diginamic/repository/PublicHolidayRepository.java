package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.PublicHoliday;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {
	PublicHoliday findOneById(final long id);

	boolean existsByDate(final LocalDate date);

	@Query("""
		SELECT ph
		FROM PublicHoliday ph
		WHERE YEAR(ph.date) = :year
	""")
	List<PublicHoliday> findByYear(final int year);

	@Query("""
		SELECT ph
		FROM PublicHoliday ph
		WHERE YEAR(ph.date) = :year
		AND MONTH(ph.date) = :month
	""")
	List<PublicHoliday> findByMonthAndYear(final int month, final int year);

	@Modifying
	@Transactional
	@Query(value="TRUNCATE TABLE public_holiday", nativeQuery=true)
	void clearTable();
}