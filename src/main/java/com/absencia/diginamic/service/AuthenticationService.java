package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.model.PostLoginModel;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final UserService userService;

	public AuthenticationService(final AuthenticationManager authenticationManager, final JwtService jwtService, final UserService userService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.userService = userService;
	}

	public String authenticate(final PostLoginModel model) throws AuthenticationException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getEmail(), model.getPassword()));

		final User user = userService.loadUserByUsername(model.getEmail());

		return jwtService.buildJwt(user);
	}
}