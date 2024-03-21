package com.absencia.diginamic.controller;

import com.absencia.diginamic.model.User;

import java.security.Principal;

import org.aspectj.bridge.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.absencia.diginamic.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUser(Authentication authentication, Principal userPrincipal) {

        System.out.println(userPrincipal.toString());

        String username = authentication.getName();
        User currentUser = userService.findOne(username);
        return ResponseEntity.ok(currentUser);
    }

}
