package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.User.Manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
	Manager findOneById(final long id);
	Manager findOneByEmail(final String email);
}