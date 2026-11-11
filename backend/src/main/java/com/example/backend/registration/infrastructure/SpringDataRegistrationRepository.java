package com.example.backend.registration.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.registration.domain.Email;
import com.example.backend.registration.domain.Registration;
import com.example.backend.registration.domain.RegistrationId;

interface SpringDataRegistrationRepository extends JpaRepository<Registration, RegistrationId> {

    List<Registration> findAllByOrderByEmailAsc();

    boolean existsByEmail(Email email);

    boolean existsByEmailAndIdValueNot(Email email, String id);
}
