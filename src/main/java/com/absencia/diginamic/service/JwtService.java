package com.absencia.diginamic.service;

import com.absencia.diginamic.configuration.JwtConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
	private final JwtConfiguration jwtConfiguration;

	public JwtService(final JwtConfiguration jwtConfiguration) {
		this.jwtConfiguration = jwtConfiguration;
	}

	/**
	 * @param userDetails The user details to get the username from
	 */
	public String buildJwt(final UserDetails userDetails) {
		final Date issuedAt = new Date();
		final Date expiration = new Date(issuedAt.getTime() + jwtConfiguration.getExpiration() * 1000);

		return Jwts
			.builder()
			.subject(userDetails.getUsername())
			.issuedAt(issuedAt)
			.expiration(expiration)
			.signWith(jwtConfiguration.getSecretKey())
			.compact();
	}

	/**
	 * @param jwt The JWT to parse
	 */
	public Claims parseClaims(final String jwt) {
		return Jwts
			.parser()
			.verifyWith(jwtConfiguration.getSecretKey())
			.build()
			.parseSignedClaims(jwt)
			.getPayload();
	}
}