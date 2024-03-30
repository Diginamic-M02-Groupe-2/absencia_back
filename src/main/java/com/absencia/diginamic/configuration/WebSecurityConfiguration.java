package com.absencia.diginamic.configuration;

import com.absencia.diginamic.service.JwtService;
import com.absencia.diginamic.service.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableMethodSecurity(prePostEnabled=true, securedEnabled=true)
@EnableWebSecurity
public class WebSecurityConfiguration {
	private final JwtAuthenticationEntryPoint unauthorizedHandler;
	private final JwtService jwtService;
	private final UserService userService;

	public WebSecurityConfiguration(final JwtAuthenticationEntryPoint unauthorizedHandler, final JwtService jwtService, final UserService userService) {
		this.unauthorizedHandler = unauthorizedHandler;
		this.jwtService = jwtService;
		this.userService = userService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
		return http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurer -> {
				final CorsConfiguration corsConfiguration = new CorsConfiguration();

				corsConfiguration.applyPermitDefaultValues();
				corsConfiguration.addAllowedMethod("PATCH");
				corsConfiguration.addAllowedMethod("DELETE");

				return corsConfiguration;
			}))
			.exceptionHandling(customizer -> customizer.authenticationEntryPoint(unauthorizedHandler))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/api/login").permitAll()
				.anyRequest().authenticated()
			)
			.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public AuthenticationFilter authenticationFilter() {
		return new AuthenticationFilter(jwtService, userService);
	}

	@Bean
	public AuthenticationManager authenticationManager(final UserDetailsService userDetailsService, final PasswordEncoder passwordEncoder) {
		final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}