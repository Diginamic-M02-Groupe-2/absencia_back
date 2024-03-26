package com.absencia.diginamic.config;

import com.absencia.diginamic.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	private JwtTokenUtil jwtTokenUtil;
	private UserService userService;

	@Autowired
	public WebSecurityConfig(final JwtAuthenticationEntryPoint unauthorizedHandler, final JwtTokenUtil jwtTokenUtil, final UserService userService) {
		this.unauthorizedHandler = unauthorizedHandler;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
	}

	@Bean
	public AuthenticationManager authenticationManager(final HttpSecurity http, final BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder
			.userDetailsService(userService)
			.passwordEncoder(bCryptPasswordEncoder);

		return authenticationManagerBuilder.build();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
		return http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurer -> {
				final CorsConfiguration corsConfiguration = new CorsConfiguration();

				corsConfiguration.applyPermitDefaultValues();
				corsConfiguration.setAllowedMethods(List.of("PATCH", "DELETE"));

				return corsConfiguration;
			}))
			.exceptionHandling(customizer -> customizer.authenticationEntryPoint(unauthorizedHandler))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/api/login").permitAll()
				.anyRequest().authenticated())
			.logout(logout -> logout.logoutUrl("/api/logout"))
			.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
		return new JwtAuthenticationFilter(jwtTokenUtil, userService);
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SessionCreationPolicy sessionCreationPolicy() {
		return SessionCreationPolicy.STATELESS;
	}
}