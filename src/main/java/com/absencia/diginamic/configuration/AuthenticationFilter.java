package com.absencia.diginamic.configuration;

import com.absencia.diginamic.service.JwtService;
import com.absencia.diginamic.service.UserService;

import io.jsonwebtoken.Claims;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthenticationFilter extends OncePerRequestFilter {
	private final JwtConfiguration jwtConfiguration;
	private final JwtService jwtService;
	private final UserService userService;

	public AuthenticationFilter(final JwtConfiguration jwtConfiguration, final JwtService jwtService, final UserService userService) {
		this.jwtConfiguration = jwtConfiguration;
		this.jwtService = jwtService;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final FilterChain filterChain) throws IOException, ServletException {
		if (request.getCookies() == null || SecurityContextHolder.getContext().getAuthentication() != null) {
			filterChain.doFilter(request, response);

			return;
		}

		final List<String> jwts = Stream
			.of(request.getCookies())
			.filter(cookie -> cookie.getName().equals(jwtConfiguration.getCookieName()))
			.map(Cookie::getValue)
			.collect(Collectors.toList());

		for (final String jwt : jwts) {
			final Claims claims = jwtService.parseClaims(jwt);
			final String username = claims.getSubject();

			if (username == null) {
				continue;
			}

			final Date expiration = claims.getExpiration();
			final UserDetails userDetails = userService.loadUserByUsername(username);

			if (expiration.before(new Date())) {
				continue;
			}

			final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			break;
		}

		filterChain.doFilter(request, response);
	}
}