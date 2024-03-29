package com.absencia.diginamic.config;

import com.absencia.diginamic.constants.JWTConstants;
import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private JwtTokenUtil jwtTokenUtil;
	private UserService userService;

	@Autowired
	public JwtAuthenticationFilter(final JwtTokenUtil jwtTokenUtil, final UserService userService) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final FilterChain chain) throws IOException, ServletException {
		String header = request.getHeader(JWTConstants.HEADER_STRING);
		String username = null;
		String authToken = null;

		if (header != null && header.startsWith(JWTConstants.TOKEN_PREFIX)) {
			authToken = header.replace(JWTConstants.TOKEN_PREFIX, "");

			try {
				// logger.error(authToken);
				username = jwtTokenUtil.getEmailFromToken(authToken);
				// logger.error("username: " + username);
			} catch (IllegalArgumentException e) {
				// logger.error("an error occured during getting username from token", e);
			} catch (ExpiredJwtException e) {
				// logger.warn("the token is expired and not valid anymore", e);
			} catch (SignatureException e) {
				// logger.error("Authentication Failed. Email or Password not valid.", e);
			}
		} else {
			// logger.warn("couldn't find bearer string, will ignore the header");
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			final User user = userService.loadUserByUsername(username);

			if (jwtTokenUtil.validateToken(authToken, user)) {
				final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);
	}
}