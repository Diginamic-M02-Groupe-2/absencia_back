package com.absencia.diginamic.Converter;

import com.absencia.diginamic.Model.AbsenceRequest.AbsenceRequestStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AbsenceRequestStatusAttributeConverter implements AttributeConverter<AbsenceRequestStatus, Integer> {
	@Override
	public Integer convertToDatabaseColumn(final AbsenceRequestStatus absenceRequestStatus) {
		if (absenceRequestStatus == null) {
			return null;
		}

		return absenceRequestStatus.value;
	}

	@Override
	public AbsenceRequestStatus convertToEntityAttribute(final Integer id) {
		if (id == null) {
			return null;
		}

		return AbsenceRequestStatus.values()[id];
	}
}