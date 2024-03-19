package com.absencia.diginamic.exception;

import org.springframework.security.core.AuthenticationException;

public class MissingJwtException extends AuthenticationException {
	public MissingJwtException(final String message) {
		super(message);
	}
}