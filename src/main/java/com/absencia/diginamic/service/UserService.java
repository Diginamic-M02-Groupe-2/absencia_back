package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.User;
import com.absencia.diginamic.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
	private UserRepository userRepository;

	@Autowired
	public UserService(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = userRepository.findOneByEmailAndDeletedAtIsNull(username);

		if (user == null) {
			throw new UsernameNotFoundException("Invalid credentials.");
		}

		return user;
	}

	public void save(final User user) {
		userRepository.save(user);
	}

	public User find(final Long id) {
		return userRepository.findOneById(id);
	}

	public User findOneByEmailAndDeletedAtIsNull(final String email) {
		return userRepository.findOneByEmailAndDeletedAtIsNull(email);
	}
}