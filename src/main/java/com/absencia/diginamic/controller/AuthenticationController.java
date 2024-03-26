package com.absencia.diginamic.controller;

import com.absencia.diginamic.config.JwtTokenUtil;
import com.absencia.diginamic.config.WebSecurityConfig;
import com.absencia.diginamic.model.LoginModel;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Import(WebSecurityConfig.class)
@RequestMapping("/api")
public class AuthenticationController {
	private AuthenticationManager authenticationManager;
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	public AuthenticationController(final AuthenticationManager authenticationManager, final JwtTokenUtil jwtTokenUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@PostMapping(value="/login", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> login(@ModelAttribute @Valid final LoginModel request) {
		final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
		final Authentication authentication = authenticationManager.authenticate(authenticationToken);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		final String token = jwtTokenUtil.generateToken(request.getEmail());

		return ResponseEntity.ok(Map.of("token", token));
	}

	@PostMapping(value="/logout")
	public ResponseEntity<Map<String, String>> logout() {
		SecurityContextHolder.getContext().setAuthentication(null);

		return ResponseEntity.ok(Map.of("message", "Vous avez été déconnecté(e)."));
	}
}