package com.absencia.diginamic.controller;

import com.absencia.diginamic.Model.User;
import com.absencia.diginamic.config.JwtTokenUtil;
import com.absencia.diginamic.config.WebSecurityConfig;
import com.absencia.diginamic.dto.AuthToken;
import com.absencia.diginamic.dto.LoginUser;
import com.absencia.diginamic.service.UserService;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Import(WebSecurityConfig.class)
@RequestMapping("/api")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestBody LoginUser loginUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final User user = userService.findOne(loginUser.getEmail());
        final String token = jwtTokenUtil.generateToken(user);

        return ResponseEntity.ok(new AuthToken(token));
    }
}
