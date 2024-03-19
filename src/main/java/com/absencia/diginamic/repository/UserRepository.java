package com.absencia.diginamic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.absencia.diginamic.Model.User.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}