package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.AbsenceType;
import com.absencia.diginamic.entity.User.Service;
import com.absencia.diginamic.entity.User.User;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, Long> {
	AbsenceRequest findOneByIdAndDeletedAtIsNull(final long id);
	List<AbsenceRequest> findByUserAndDeletedAtIsNull(final User user);
	List<AbsenceRequest> findInitial();
	long countRemaining(final User user, final AbsenceType type);
	boolean isOverlapping(
		final Long id,
		final User user,
		final LocalDate startedAt,
		final LocalDate endedAt
	);

	List<AbsenceRequest> findByMonthYearAndService(final int month, final int year, final Service service);
	int getDataForDayForEmployee(final Long employeeId, final LocalDate date);
}