package com.absencia.diginamic.controller;

import com.absencia.diginamic.model.User;
import com.absencia.diginamic.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/current")
	public ResponseEntity<User> getCurrentUser(final Authentication authentication) {
		final User user = userService.findOneByEmailAndDeletedAtIsNull(authentication.getName());

		return ResponseEntity.ok(user);
	}
}