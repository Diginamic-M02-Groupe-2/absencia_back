package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.User.Manager;
import com.absencia.diginamic.repository.ManagerRepository;

import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ManagerService implements UserDetailsService {
	private ManagerRepository managerRepository;

	public ManagerService(final ManagerRepository managerRepository) {
		this.managerRepository = managerRepository;
	}

	@Override
	public Manager loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Manager manager = managerRepository.findOneByEmail(username);

		if (manager == null) {
			throw new UsernameNotFoundException("Invalid credentials.");
		}

		return manager;
	}

	public void save(@NonNull final Manager manager) {
		managerRepository.save(manager);
	}

	public Manager find(final long id) {
		return managerRepository.findOneById(id);
	}
}