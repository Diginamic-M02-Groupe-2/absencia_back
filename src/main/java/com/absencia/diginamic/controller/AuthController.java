package com.absencia.diginamic.controller;

import com.absencia.diginamic.config.JwtTokenUtil;
import com.absencia.diginamic.config.WebSecurityConfig;
import com.absencia.diginamic.dto.LoginUser;

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
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping(value="/login")
	public ResponseEntity<String> login(@RequestBody final LoginUser loginUser) {
		final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword());
		final Authentication authentication = authenticationManager.authenticate(authenticationToken);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		final String token = jwtTokenUtil.generateToken(loginUser.getEmail());

		return ResponseEntity.ok(token);
	}
}