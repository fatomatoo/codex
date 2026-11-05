package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, String> {

    List<Registration> findAllByOrderByCreatedAtDesc();

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, String id);
}
