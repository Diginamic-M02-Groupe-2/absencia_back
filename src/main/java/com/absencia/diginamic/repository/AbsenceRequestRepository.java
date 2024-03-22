package com.absencia.diginamic.repository;

import com.absencia.diginamic.model.AbsenceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbsenceRequestRepository extends JpaRepository<AbsenceRequest, Long> {

    List<AbsenceRequest> findByUserId(Long userId);
}