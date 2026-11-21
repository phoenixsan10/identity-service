package com.phoenixsan.identity.identity_service.master.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phoenixsan.identity.identity_service.master.dto.OrganizationRequest;
import com.phoenixsan.identity.identity_service.master.dto.OrganizationResponse;
import com.phoenixsan.identity.identity_service.master.service.OrganizationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {

    private final OrganizationService service;

    @PostMapping
    public OrganizationResponse create(@RequestBody OrganizationRequest request) {
        log.info("Creating new organization");
        return service.create(request);
    }

    @PutMapping("/{id}")
    public OrganizationResponse update(@PathVariable Long id,
                                       @RequestBody OrganizationRequest request) {
        log.info("Updating organization with ID: {}", id);
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public OrganizationResponse getById(@PathVariable Long id) {
        log.info("Fetching organization with ID: {}", id);
        return service.getById(id);
    }

    @GetMapping
    public List<OrganizationResponse> getAll() {
        log.info("Fetching all organizations");
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting organization with ID: {}", id);
        service.delete(id);
    }
}
