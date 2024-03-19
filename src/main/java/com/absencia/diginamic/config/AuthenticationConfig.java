package com.absencia.diginamic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthenticationConfig {

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService,
            BCryptPasswordEncoder encoder) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder);
    }
}
