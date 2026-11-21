package com.phoenixsan.identity.identity_service.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.phoenixsan.identity.identity_service.exception.ResourceNotFoundException;
import com.phoenixsan.identity.identity_service.master.dto.OrganizationRequest;
import com.phoenixsan.identity.identity_service.master.dto.OrganizationResponse;
import com.phoenixsan.identity.identity_service.master.entity.Organization;
import com.phoenixsan.identity.identity_service.master.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {

    private final OrganizationRepository repository;

    public OrganizationResponse create(OrganizationRequest request) {
        log.info("Creating new organization with code: {}", request.getCode());

        if (repository.existsByCode(request.getCode())) {
            log.warn("Organization code {} already exists", request.getCode());
            throw new RuntimeException("Organization code already exists");
        }

        Organization org = Organization.builder()
                .name(request.getName())
                .code(request.getCode())
                .status(request.getStatus())
                .build();

        Organization saved = repository.save(org);

        return mapToResponse(saved);
    }

    public OrganizationResponse update(Long id, OrganizationRequest request) {
        log.info("Updating organization with ID: {}", id);

        Organization org = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Orgnization not found"));


        org.setName(request.getName());
        org.setCode(request.getCode());
        org.setStatus(request.getStatus());

        Organization updated = repository.save(org);

        return mapToResponse(updated);
    }

    public OrganizationResponse getById(Long id) {
        log.info("Fetching organization with ID: {}", id);
        Organization org = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Orgnization not found"));


        return mapToResponse(org);
    }

    public List<OrganizationResponse> getAll() {
        log.info("Fetching all organizations");
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void delete(Long id) {
        log.info("Deleting organization with ID: {}", id);
        repository.deleteById(id);
    }

    private OrganizationResponse mapToResponse(Organization org) {
        return OrganizationResponse.builder()
                .id(org.getId())
                .name(org.getName())
                .code(org.getCode())
                .status(org.getStatus())
                .createdAt(org.getCreatedAt())
                .updatedAt(org.getUpdatedAt())
                .build();
    }
}
