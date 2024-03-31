package com.absencia.diginamic.configuration;

import com.absencia.diginamic.service.JwtService;
import com.absencia.diginamic.service.UserService;

import io.jsonwebtoken.Claims;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserService userService;

	public AuthenticationFilter(final JwtService jwtService, final UserService userService) {
		this.jwtService = jwtService;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final FilterChain filterChain) throws IOException, ServletException {
		filterInternal(request, response);

		filterChain.doFilter(request, response);
	}

	private void filterInternal(final HttpServletRequest request, final HttpServletResponse response) {
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			return;
		}

		final String authorization = request.getHeader("Authorization");

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return;
		}

		final String bearer = authorization.substring(7);
		final Claims claims = jwtService.parseClaims(bearer);
		final String username = claims.getSubject();

		if (username == null) {
			return;
		}

		final UserDetails userDetails = userService.loadUserByUsername(username);

		if (claims.getExpiration().before(new Date())) {
			return;
		}

		final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}