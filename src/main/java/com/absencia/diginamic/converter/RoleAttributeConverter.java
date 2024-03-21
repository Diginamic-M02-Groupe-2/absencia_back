package com.absencia.diginamic.converter;

import com.absencia.diginamic.model.Role;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleAttributeConverter implements AttributeConverter<Role, Integer> {
	@Override
	public Integer convertToDatabaseColumn(final Role role) {
		if (role == null) {
			return null;
		}

		return role.value;
	}

	@Override
	public Role convertToEntityAttribute(final Integer id) {
		if (id == null) {
			return null;
		}

		return Role.values()[id];
	}
}