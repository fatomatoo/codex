package com.example.backend.registration;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, String> {

    List<Registration> findAllByOrderByCreatedAtDesc();

    boolean existsByEmailIgnoreCase(String email);
}
