package com.absencia.diginamic.repository;

import com.absencia.diginamic.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findOneById(final Long id);
    User findOneByEmailAndDeletedAtIsNull(final String email);
}