package com.absencia.diginamic.controller;

import com.absencia.diginamic.config.JwtTokenUtil;
import com.absencia.diginamic.config.WebSecurityConfig;
import com.absencia.diginamic.dto.LoginUser;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Import(WebSecurityConfig.class)
@RequestMapping("/api")
public class AuthController {
	private AuthenticationManager authenticationManager;
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	public AuthController(final AuthenticationManager authenticationManager, final JwtTokenUtil jwtTokenUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@PostMapping(value="/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody final LoginUser loginUser) {
		final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword());
		final Authentication authentication = authenticationManager.authenticate(authenticationToken);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		final String token = jwtTokenUtil.generateToken(loginUser.getEmail());

		return ResponseEntity.ok(Map.of("token", token));
	}

	@PostMapping(value="/logout")
	public ResponseEntity<Map<String, String>> logout() {
		SecurityContextHolder.getContext().setAuthentication(null);

		return ResponseEntity.ok(Map.of("message", "Vous avez été déconnecté(e)."));
	}
}