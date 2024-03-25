package com.absencia.diginamic.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.AbsenceRequestStatus;
import com.absencia.diginamic.entity.AbsenceType;
import com.absencia.diginamic.entity.User;

@Repository
public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, Long> {
	AbsenceRequest findOneById(final Long id);
	List<AbsenceRequest> findByUser(final User user);
	long countByIdAndUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndDeletedAtIsNull(final Long id, final User user, final Date startedAt, final Date endedAt);
	long countByUserAndStatusAndTypeAndDeletedAtIsNull(final User user, final AbsenceRequestStatus status, final AbsenceType type);
}