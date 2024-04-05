package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.EmployerWtr;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerWtrRepository extends JpaRepository<EmployerWtr, Long> {
	EmployerWtr findOneByIdAndDeletedAtIsNull(final Long id);

	boolean existsByDate(final LocalDate date);

	@Query("""
		SELECT ew
		FROM EmployerWtr ew
		WHERE ew.deletedAt IS NULL
		AND YEAR(ew.date) = :year
	""")
	List<EmployerWtr> findByYear(final int year);

	@Query("""
		SELECT ew
		FROM EmployerWtr ew
		WHERE ew.deletedAt IS NULL
		AND ew.status = EmployerWtrStatus.INITIAL
		ORDER BY ew.date ASC
	""")
	List<EmployerWtr> findInitial();

	@Query("""
		SELECT COUNT(1)
		FROM EmployerWtr ew
		WHERE ew.deletedAt IS NULL
		AND ew.status = EmployerWtrStatus.APPROVED
	""")
	long countApproved();

	@Query("""
		SELECT COUNT(ew) > 0
		FROM EmployerWtr ew
		WHERE ew.id <> :id
		AND ew.date = :date
		AND ew.deletedAt IS NULL
	""")
	boolean isDateConflictingWithOther(final Long id, final LocalDate date);
}