package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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

	@Override
	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = userRepository.findOneByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException("Invalid credentials.");
		}

		return user;
	}

	public void save(@NonNull final User user) {
		userRepository.save(user);
	}

	public User find(final Long id) {
		return userRepository.findOneById(id);
	}
}