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

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService service;

    @PostMapping
    public OrganizationResponse create(@RequestBody OrganizationRequest request) {
        
        return service.create(request);
    }

    @PutMapping("/{id}")
    public OrganizationResponse update(@PathVariable Long id,
                                       @RequestBody OrganizationRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public OrganizationResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<OrganizationResponse> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
