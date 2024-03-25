package com.absencia.diginamic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.absencia.diginamic.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findOneById(final Long id);
    User findOneByEmailAndDeletedAtIsNull(final String email);
}