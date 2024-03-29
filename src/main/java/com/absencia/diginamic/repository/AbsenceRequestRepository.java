package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.AbsenceType;
import com.absencia.diginamic.entity.User.User;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, Long> {
	AbsenceRequest findOneByIdAndDeletedAtIsNull(final Long id);
	List<AbsenceRequest> findByUserAndDeletedAtIsNull(final User user);
	boolean isOverlapping(final Long id, final User user, final LocalDate startedAt, final LocalDate endedAt);
	long countRemaining(final User user, final AbsenceType type);
}