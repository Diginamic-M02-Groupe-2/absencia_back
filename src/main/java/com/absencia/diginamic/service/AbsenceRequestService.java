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

	/**
	 * @param absenceRequest The absence request to save
	 */
	public void save(@NonNull final AbsenceRequest absenceRequest) {
		absenceRequestRepository.save(absenceRequest);
	}

	/**
	 * @param absenceRequest The absence request to delete
	 */
	public void delete(final AbsenceRequest absenceRequest) {
		absenceRequest.setDeletedAt(LocalDate.now());

		save(absenceRequest);
	}

	/**
	 * @param user The ID of the absence request to get
	 */
	public AbsenceRequest find(final long id) {
		return absenceRequestRepository.findOneByIdAndDeletedAtIsNull(id);
	}

	/**
	 * @param user The user to get the absence requests from
	 */
	public List<AbsenceRequest> findByUser(final User user) {
		return absenceRequestRepository.findByUserAndDeletedAtIsNull(user);
	}

	public List<AbsenceRequest> findInitial() {
		return absenceRequestRepository.findInitial();
	}

	/**
	 * @param user The user to count the remaining paid leave days from
	 */
	public long countRemainingPaidLeaves(final User user) {
		final long sum = absenceRequestRepository.sumApprovedDays(user.getId(), AbsenceType.PAID_LEAVE);

		return MAX_PAID_LEAVES - sum;
	}

	/**
	 * @param user The user to count the remaining employee WTR days from
	 */
	public long countRemainingEmployeeWtr(final User user) {
		final long sum = absenceRequestRepository.sumApprovedDays(user.getId(), AbsenceType.EMPLOYEE_WTR);

		return MAX_EMPLOYEE_WTR - sum;
	}

	/**
	 * @param absenceRequest The absence request to check for overlaps with others
	 */
	public boolean isOverlapping(final AbsenceRequest absenceRequest) {
		final boolean isOverlapping = absenceRequestRepository.isOverlapping(
			absenceRequest.getId(), // Ignore this absence request
			absenceRequest.getUser(), // Filter by the same user
			absenceRequest.getStartedAt(),
			absenceRequest.getEndedAt()
		);

		return isOverlapping;
	}

	/**
	 * @param month The month to filter the absence requests from
	 * @param year The year to filter the absence requests from
	 * @param service The service to filter the absence requests from
	 */
	public List<AbsenceRequest> findByMonthYearAndService(final int month, final int year, final com.absencia.diginamic.entity.User.Service service) {
		return absenceRequestRepository.findByMonthYearAndService(month, year, service);
	}

	/**
	 * @param employeeId The ID of the employee to get the data from
	 * @param year The year to build the date object
	 * @param month The month to build the date object
	 * @param day The day to build the date object
	 */
	public int getDataForDay(final Long employeeId, final int year, final int month, final int day) {
		final LocalDate date = LocalDate.of(year, month, day);

		return absenceRequestRepository.getDataForDayForEmployee(employeeId, date);
	}
}