package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.AbsenceType;
import com.absencia.diginamic.entity.User.Service;
import com.absencia.diginamic.entity.User.User;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, Long> {
	AbsenceRequest findOneByIdAndDeletedAtIsNull(final long id);

	List<AbsenceRequest> findByUserAndDeletedAtIsNull(final User user);

	@Query("""
		SELECT ar
		FROM AbsenceRequest ar
		WHERE ar.deletedAt IS NULL
		AND ar.status = AbsenceRequestStatus.INITIAL
		ORDER BY ar.startedAt ASC
	""")
	List<AbsenceRequest> findInitial();

	@Query("""
		SELECT COALESCE(SUM(DATEDIFF(ar.endedAt, ar.startedAt) + 1), 0)
		FROM AbsenceRequest ar
		WHERE ar.deletedAt IS NULL
		AND ar.user.id = :userId
		AND ar.type = :type
		AND ar.status = AbsenceRequestStatus.APPROVED
	""")
	long sumApprovedDays(final long userId, final AbsenceType type);

	@Query("""
		SELECT CASE WHEN COUNT(ar) > 0 THEN true ELSE false END
		FROM AbsenceRequest ar
		WHERE ar.deletedAt IS NULL
		AND (:id IS NULL OR ar.id != :id)
		AND ar.user = :user	
		AND ar.startedAt < :endedAt
		AND ar.endedAt > :startedAt
	""")
	boolean isOverlapping(
		final Long id,
		final User user,
		final LocalDate startedAt,
		final LocalDate endedAt
	);

	@Query("""
		SELECT ar
		FROM AbsenceRequest ar
		JOIN ar.user u
		WHERE ar.deletedAt IS NULL
		AND FUNCTION('YEAR', ar.startedAt) = :year
		AND FUNCTION('MONTH', ar.startedAt) = :month
		AND u.service = :service
	""")
	List<AbsenceRequest> findByMonthYearAndService(final int month, final int year, final Service service);

	@Query("""
		SELECT COUNT(ar) FROM AbsenceRequest ar
		WHERE ar.deletedAt IS NULL
		AND ar.user.id = :employeeId
		AND :date BETWEEN ar.startedAt AND ar.endedAt
	""")
	int getDataForDayForEmployee(final Long employeeId, final LocalDate date);

	@Query("""
		SELECT ar FROM AbsenceRequest ar 
		WHERE ar.deletedAt IS NULL
		AND ar.user.service = :service
		AND YEAR(ar.startedAt) = :year
		AND MONTH(ar.startedAt) = :month
		AND ar.status = AbsenceRequestStatus.APPROVED
		AND ar.user.id IN :employeeIds
	""")
	List<AbsenceRequest> findApprovedByMonthYearAndServiceAndEmployees(int month, int year, Service service, List<Long> employeeIds);
}