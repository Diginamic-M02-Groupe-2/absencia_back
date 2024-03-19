package com.absencia.diginamic.Converter;

import com.absencia.diginamic.Model.Service;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ServiceAttributeConverter implements AttributeConverter<Service, Integer> {
	@Override
	public Integer convertToDatabaseColumn(final Service service) {
		if (service == null) {
			return null;
		}

		return service.value;
	}

	@Override
	public Service convertToEntityAttribute(final Integer id) {
		if (id == null) {
			return null;
		}

		return Service.values()[id];
	}
}