package com.absencia.diginamic.service;

import com.absencia.diginamic.configuration.JwtConfiguration;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.model.PostLoginModel;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
	private AuthenticationManager authenticationManager;
	private JwtConfiguration jwtConfiguration;
	private JwtService jwtService;
	private UserService userService;

	public AuthenticationService(final AuthenticationManager authenticationManager, final JwtConfiguration jwtConfiguration, final JwtService jwtService, final UserService userService) {
		this.authenticationManager = authenticationManager;
		this.jwtConfiguration = jwtConfiguration;
		this.jwtService = jwtService;
		this.userService = userService;
	}

	public ResponseCookie authenticate(final PostLoginModel model) throws AuthenticationException {
		final User user;

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getEmail(), model.getPassword()));

			user = userService.loadUserByUsername(model.getEmail());
		} catch (final AuthenticationException exception) {
			throw new UsernameNotFoundException("Identifiants invalides.");
		}

		return ResponseCookie
			.from(jwtConfiguration.getCookieName(), jwtService.buildJwt(user))
			.httpOnly(true)
			.secure(true)
			.build();
	}
}