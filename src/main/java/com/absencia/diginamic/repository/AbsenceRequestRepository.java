package com.absencia.diginamic.repository;

import com.absencia.diginamic.model.AbsenceRequest;
import com.absencia.diginamic.model.AbsenceRequestStatus;
import com.absencia.diginamic.model.AbsenceType;
import com.absencia.diginamic.model.User;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, Long> {
	AbsenceRequest findOneById(final Long id);
	List<AbsenceRequest> findByUser(final User user);
	long countByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndDeletedAtIsNull(final User user, final Date startedAt, final Date endedAt);
	long countByUserAndStatusAndTypeAndDeletedAtIsNull(final User user, final AbsenceRequestStatus status, final AbsenceType type);
}