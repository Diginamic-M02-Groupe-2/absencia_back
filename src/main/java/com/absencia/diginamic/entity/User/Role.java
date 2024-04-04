package com.absencia.diginamic.entity.User;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	ADMINISTRATOR(0),
	EMPLOYEE(1),
	MANAGER(2);

	public final int value;

	private Role(final int value) {
		this.value = value;
	}

	@Override
	public String getAuthority() {
		return name();
	}
}