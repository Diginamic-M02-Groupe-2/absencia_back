package com.absencia.diginamic.Converter;

import com.absencia.diginamic.Model.Absence.AbsenceType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AbsenceTypeAttributeConverter implements AttributeConverter<AbsenceType, Integer> {
	@Override
	public Integer convertToDatabaseColumn(final AbsenceType absenceType) {
		if (absenceType == null) {
			return null;
		}

		return absenceType.value;
	}

	@Override
	public AbsenceType convertToEntityAttribute(final Integer id) {
		if (id == null) {
			return null;
		}

		return AbsenceType.values()[id];
	}
}