package com.absencia.diginamic.service;

import com.absencia.diginamic.model.AbsenceRequest;
import com.absencia.diginamic.model.AbsenceRequestStatus;
import com.absencia.diginamic.model.AbsenceType;
import com.absencia.diginamic.model.User;
import com.absencia.diginamic.repository.AbsenceRequestRepository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbsenceRequestService {
	private AbsenceRequestRepository absenceRequestRepository;

	@Autowired
	public AbsenceRequestService(final AbsenceRequestRepository absenceRequestRepository) {
		this.absenceRequestRepository = absenceRequestRepository;
	}

	public void save(final AbsenceRequest absenceRequest) {
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
		return absenceRequestRepository.countByUserAndStatusAndTypeAndDeletedAtIsNull(user, AbsenceRequestStatus.APPROVED, AbsenceType.PAID_LEAVE);
	}

	public boolean isOverlapping(final AbsenceRequest absenceRequest) {
		final long count = absenceRequestRepository.countByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndDeletedAtIsNull(absenceRequest.getUser(), absenceRequest.getAbsence().getStartedAt(), absenceRequest.getAbsence().getEndedAt());

		return count != 0;
	}
}