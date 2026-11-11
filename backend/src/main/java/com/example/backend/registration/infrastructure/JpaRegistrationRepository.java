package com.example.backend.registration.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.example.backend.registration.domain.Email;
import com.example.backend.registration.domain.EmailAlreadyRegisteredException;
import com.example.backend.registration.domain.Registration;
import com.example.backend.registration.domain.RegistrationId;
import com.example.backend.registration.domain.RegistrationRepository;

@Repository
class JpaRegistrationRepository implements RegistrationRepository {

    private final SpringDataRegistrationRepository jpa;

    JpaRegistrationRepository(SpringDataRegistrationRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Registration> findAll() {
        return jpa.findAllByOrderByEmailAsc();
    }

    @Override
    public Optional<Registration> findById(RegistrationId id) {
        return jpa.findById(id);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpa.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailExcept(Email email, RegistrationId id) {
        return jpa.existsByEmailAndIdValueNot(email, id.value());
    }

    @Override
    public boolean isEmpty() {
        return jpa.count() == 0;
    }

    @Override
    public Registration save(Registration registration) {
        try {
            // Flush now so a unique-constraint violation (concurrent signup) surfaces here.
            return jpa.saveAndFlush(registration);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyRegisteredException(registration.getEmail());
        }
    }

    @Override
    public void delete(Registration registration) {
        jpa.delete(registration);
    }
}
