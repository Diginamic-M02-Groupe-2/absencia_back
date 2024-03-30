package com.absencia.diginamic.controller;

import com.absencia.diginamic.configuration.WebSecurityConfiguration;
import com.absencia.diginamic.model.PostLoginModel;
import com.absencia.diginamic.service.AuthenticationService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Import(WebSecurityConfiguration.class)
@RequestMapping("/api")
public class AuthenticationController {
	private final AuthenticationService authenticationService;

	public AuthenticationController(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping(value="/login", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> login(@ModelAttribute @Valid final PostLoginModel model) {
		final ResponseCookie cookie;

		try {
			cookie = authenticationService.authenticate(model);
		} catch (final AuthenticationException exception) {
			return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(Map.of("email", exception.getMessage()));
		}

		return ResponseEntity
			.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.build();
	}

	@PostMapping(value="/logout")
	public ResponseEntity<Map<String, String>> logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();

		return ResponseEntity.ok(Map.of("message", "Vous avez été déconnecté(e)."));
	}
}