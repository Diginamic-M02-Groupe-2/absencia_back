package com.absencia.diginamic.service;

import com.absencia.diginamic.entity.User.Administrator;
import com.absencia.diginamic.repository.AdministratorRepository;

import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService implements UserDetailsService {
	private AdministratorRepository administratorRepository;

	public AdministratorService(final AdministratorRepository administratorRepository) {
		this.administratorRepository = administratorRepository;
	}

	@Override
	public Administrator loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Administrator administrator = administratorRepository.findOneByEmail(username);

		if (administrator == null) {
			throw new UsernameNotFoundException("Invalid credentials.");
		}

		return administrator;
	}

	public void save(@NonNull final Administrator administrator) {
		administratorRepository.save(administrator);
	}

	public Administrator find(final long id) {
		return administratorRepository.findOneById(id);
	}
}