package com.absencia.diginamic.task;

import com.absencia.diginamic.entity.AbsenceRequest;
import com.absencia.diginamic.entity.AbsenceRequestStatus;
import com.absencia.diginamic.entity.AbsenceType;
import com.absencia.diginamic.entity.EmployerWtr;
import com.absencia.diginamic.entity.EmployerWtrStatus;
import com.absencia.diginamic.service.AbsenceRequestService;
import com.absencia.diginamic.service.EmployerWtrService;

import java.time.Period;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NightTask {
	private static final Logger logger = LoggerFactory.getLogger(NightTask.class);

	private final AbsenceRequestService absenceRequestService;
	private final EmployerWtrService employerWtrService;

	public NightTask(final AbsenceRequestService absenceRequestService, final EmployerWtrService employerWtrService) {
		this.absenceRequestService = absenceRequestService;
		this.employerWtrService = employerWtrService;
	}

	@Scheduled(cron="0 0 0 * * *")
	public void run() {
		final List<AbsenceRequest> initialAbsenceRequests = absenceRequestService.findInitial();

		logger.info("Found {} absence requests.", initialAbsenceRequests.size());

		for (final AbsenceRequest absenceRequest : initialAbsenceRequests) {
			logger.info("Processing absence request #{}...", absenceRequest.getId());

			if (hasEnoughDays(absenceRequest)) {
				absenceRequest.setStatus(AbsenceRequestStatus.PENDING);

				logger.info("  Marked as pending.", absenceRequest.getId());
			} else {
				absenceRequest.setStatus(AbsenceRequestStatus.REJECTED);

				logger.info("  Rejected.");
			}

			absenceRequestService.save(absenceRequest);
		}

		logger.info("Finished processing absence requests.");

		final List<EmployerWtr> employerWtrList = employerWtrService.findInitial();

		for (final EmployerWtr employerWtr : employerWtrList) {
			logger.info("Processing employer WTR #{}...", employerWtr.getId());

			employerWtr.setStatus(EmployerWtrStatus.APPROVED);

			logger.info("  Marked as approved.");

			employerWtrService.save(employerWtr);
		}
	}

	private boolean hasEnoughDays(final AbsenceRequest absenceRequest) {
		if (absenceRequest.getType() == AbsenceType.UNPAID_LEAVE) {
			return true;
		}

		final long requestedDays = Period.between(absenceRequest.getStartedAt(), absenceRequest.getEndedAt()).getDays() + 1;
		final long remainingDays = absenceRequest.getType() == AbsenceType.PAID_LEAVE ?
			absenceRequestService.countRemainingPaidLeaves(absenceRequest.getUser()) :
			absenceRequestService.countRemainingEmployeeWtr(absenceRequest.getUser());

		return remainingDays >= requestedDays;
	}
}