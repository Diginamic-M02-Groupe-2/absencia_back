package com.absencia.diginamic.converter;

import com.absencia.diginamic.model.AbsenceRequestStatus;

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