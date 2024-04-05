package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.User.User;
import com.absencia.diginamic.repository.UserRepository;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;

	public UserService(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * @param username The username to load the user by
	 */
	@Override
	public User loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = userRepository.findOneByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException("Identifiants invalides.");
		}

		return user;
	}

	/**
	 * @param user The user to save
	 */
	public void save(@NonNull final User user) {
		userRepository.save(user);
	}

	/**
	 * @param id The ID of the user to get
	 */
	public User find(final long id) {
		return userRepository.findOneById(id);
	}

	/**
	 * @param managerId The ID of manager
	 */
	public List<User> findEmployeesManagedByManager(long managerId) {
		return userRepository.findByManagerId(managerId);
	}
}