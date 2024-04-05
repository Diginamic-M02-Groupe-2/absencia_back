package com.absencia.diginamic.repository;

import com.absencia.diginamic.entity.User.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findOneById(final long id);
	User findOneByEmail(final String email);
	@Query("SELECT e.id FROM Employee e WHERE e.manager.id = :managerId")
	List<Long> findByManagerId(final Long managerId);

}