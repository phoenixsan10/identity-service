package com.phoenixsan.identity.identity_service.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.phoenixsan.identity.identity_service.exception.ResourceNotFoundException;
import com.phoenixsan.identity.identity_service.master.dto.OrganizationRequest;
import com.phoenixsan.identity.identity_service.master.dto.OrganizationResponse;
import com.phoenixsan.identity.identity_service.master.entity.Organization;
import com.phoenixsan.identity.identity_service.master.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;

    public OrganizationResponse create(OrganizationRequest request) {

        if (repository.existsByCode(request.getCode())) {
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

        Organization org = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Orgnization not found"));


        org.setName(request.getName());
        org.setCode(request.getCode());
        org.setStatus(request.getStatus());

        Organization updated = repository.save(org);

        return mapToResponse(updated);
    }

    public OrganizationResponse getById(Long id) {
        Organization org = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Orgnization not found"));


        return mapToResponse(org);
    }

    public List<OrganizationResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void delete(Long id) {
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
