package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.EmailAvailability;
import com.example.backend.dto.RegistrationRequest;
import com.example.backend.dto.RegistrationResponse;
import com.example.backend.service.RegistrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService service;

    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    @GetMapping
    public List<RegistrationResponse> list() {
        return service.findAll();
    }

    @GetMapping("/exists")
    public EmailAvailability exists(@RequestParam String email) {
        return new EmailAvailability(service.isEmailTaken(email));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse create(@Valid @RequestBody RegistrationRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public RegistrationResponse update(@PathVariable String id, @Valid @RequestBody RegistrationRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
