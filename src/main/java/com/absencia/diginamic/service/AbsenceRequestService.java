package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.AbsenceRequestStatus;
import com.absencia.diginamic.entity.AbsenceType;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.repository.AbsenceRequestRepository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class AbsenceRequestService {
	private AbsenceRequestRepository absenceRequestRepository;

	@Autowired
	public AbsenceRequestService(final AbsenceRequestRepository absenceRequestRepository) {
		this.absenceRequestRepository = absenceRequestRepository;
	}

	public void save(@NonNull final AbsenceRequest absenceRequest) {
		absenceRequestRepository.save(absenceRequest);
	}

	public void delete(final AbsenceRequest absenceRequest) {
		absenceRequest.setDeletedAt(new Date());

		save(absenceRequest);
	}

	public AbsenceRequest find(final Long id) {
		return absenceRequestRepository.findOneById(id);
	}

	public List<AbsenceRequest> findByUser(final User user) {
		return absenceRequestRepository.findByUser(user);
	}

	public long countRemainingPaidLeaves(final User user) {
		return absenceRequestRepository.countByUserAndTypeAndStatusAndDeletedAtIsNull(user, AbsenceType.PAID_LEAVE, AbsenceRequestStatus.APPROVED);
	}

	public long countRemainingEmployeeWtr(final User user) {
		return absenceRequestRepository.countByUserAndTypeAndStatusAndDeletedAtIsNull(user, AbsenceType.EMPLOYEE_WTR, AbsenceRequestStatus.APPROVED);
	}

	public boolean isOverlapping(final AbsenceRequest absenceRequest) {
		final long count = absenceRequestRepository.countByIdAndUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndDeletedAtIsNull(
			absenceRequest.getId(),
			absenceRequest.getUser(),
			absenceRequest.getStartedAt(),
			absenceRequest.getEndedAt()
		);

		return count != 0;
	}
}