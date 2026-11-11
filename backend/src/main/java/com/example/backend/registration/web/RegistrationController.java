package com.example.backend.registration.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.registration.application.RegistrationService;
import com.example.backend.registration.domain.RegistrationId;

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
        return service.findAll().stream().map(RegistrationResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse create(@Valid @RequestBody RegistrationRequest request) {
        return RegistrationResponse.from(service.register(request.toDetails()));
    }

    @PutMapping("/{id}")
    public RegistrationResponse update(@PathVariable String id, @Valid @RequestBody RegistrationRequest request) {
        return RegistrationResponse.from(service.change(new RegistrationId(id), request.toDetails()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.cancel(new RegistrationId(id));
    }
}
