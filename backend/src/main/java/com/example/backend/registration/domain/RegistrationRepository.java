package com.example.backend.registration.domain;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository {

    List<Registration> findAllNewestFirst();

    Optional<Registration> findById(RegistrationId id);

    boolean existsByEmail(Email email);

    boolean existsByEmailExcept(Email email, RegistrationId id);

    boolean isEmpty();

    /** @throws EmailAlreadyRegisteredException if the email was taken concurrently */
    Registration save(Registration registration);

    void delete(Registration registration);
}
