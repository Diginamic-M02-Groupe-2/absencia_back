package com.absencia.diginamic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.absencia.diginamic.Model.User;
import com.absencia.diginamic.exception.DuplicateUserNameException;
import com.absencia.diginamic.exception.ResourceNotFoundException;
import com.absencia.diginamic.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userId);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid email or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public User findOne(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user) {
        if (user.getEmail() == null)
            throw new ResourceNotFoundException("Username is required", "Username", user.getEmail());

        if (user.getPassword() == null)
            throw new ResourceNotFoundException("Password is required", "Password", user.getPassword());

        User usr = userRepository.findByEmail(user.getEmail());
        if (usr != null)
            throw new DuplicateUserNameException("Username Already exists.");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}