package com.absencia.diginamic.controller;

import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private UserService userService;

	@Autowired
	public UserController(final UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/current")
	public ResponseEntity<User> getCurrentUser(final Authentication authentication) {
		final User user = userService.loadUserByUsername(authentication.getName());

		return ResponseEntity.ok(user);
	}
}