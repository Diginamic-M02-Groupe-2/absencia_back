package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.AbsenceType;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.repository.AbsenceRequestRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class AbsenceRequestService {
	public static final long MAX_PAID_LEAVES = 25;
	public static final long MAX_EMPLOYEE_WTR = 6;

	private final AbsenceRequestRepository absenceRequestRepository;

	public AbsenceRequestService(final AbsenceRequestRepository absenceRequestRepository) {
		this.absenceRequestRepository = absenceRequestRepository;
	}

	public void save(@NonNull final AbsenceRequest absenceRequest) {
		absenceRequestRepository.save(absenceRequest);
	}

	public void delete(final AbsenceRequest absenceRequest) {
		absenceRequest.setDeletedAt(LocalDate.now());

		save(absenceRequest);
	}

	public AbsenceRequest find(final long id) {
		return absenceRequestRepository.findOneByIdAndDeletedAtIsNull(id);
	}

	public List<AbsenceRequest> findByUser(final User user) {
		return absenceRequestRepository.findByUserAndDeletedAtIsNull(user);
	}

	public List<AbsenceRequest> findInitial() {
		return absenceRequestRepository.findInitial();
	}

	public long countRemainingPaidLeaves(final User user) {
		final long count = absenceRequestRepository.countRemaining(user, AbsenceType.PAID_LEAVE);

		return MAX_PAID_LEAVES - count;
	}

	public long countRemainingEmployeeWtr(final User user) {
		final long count = absenceRequestRepository.countRemaining(user, AbsenceType.EMPLOYEE_WTR);

		return MAX_EMPLOYEE_WTR - count;
	}

	public boolean isOverlapping(final AbsenceRequest absenceRequest) {
		final boolean isOverlapping = absenceRequestRepository.isOverlapping(
			absenceRequest.getId(), // Ignore this absence request
			absenceRequest.getUser(), // Filter by the same user
			absenceRequest.getStartedAt(),
			absenceRequest.getEndedAt()
		);

		return isOverlapping;
	}

	public List<AbsenceRequest> findByMonthYearAndService(int month, int year, com.absencia.diginamic.entity.User.Service service) {
		return absenceRequestRepository.findByMonthYearAndService(month, year, service);
	}

	public com.absencia.diginamic.entity.User.Service getServiceById(int serviceId) {
		for (com.absencia.diginamic.entity.User.Service service : com.absencia.diginamic.entity.User.Service.values()) {
			if (service.value == serviceId) {
				return service;
			}
		}
		return null;
	}

}