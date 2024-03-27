package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.User.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findOneById(final Long id);
	User findOneByEmail(final String email);
}