package com.absencia.diginamic.configuration;

import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {
	@Value("${jwt.expiration}")
	private long expiration;

	@Value("${jwt.secret}")
	private byte[] encodedSecretKey;

	private SecretKey secretKey;

	public long getExpiration() {
		return expiration;
	}

	public SecretKey getSecretKey() {
		return secretKey;
	}

	@PostConstruct
	public void buildSecretKey() {
		secretKey = Keys.hmacShaKeyFor(encodedSecretKey);
	}
}