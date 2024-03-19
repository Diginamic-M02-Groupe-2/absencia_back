package com.absencia.diginamic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.absencia.diginamic.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}