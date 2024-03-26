package com.absencia.diginamic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.absencia.diginamic.entity.User.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findOneById(final Long id);
	User findOneByEmail(final String email);
}