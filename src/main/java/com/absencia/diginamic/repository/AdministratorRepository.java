package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.User.Administrator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
	Administrator findOneById(final long id);
	Administrator findOneByEmail(final String email);
}