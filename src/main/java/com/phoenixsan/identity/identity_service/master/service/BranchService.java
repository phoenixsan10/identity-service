package com.phoenixsan.identity.identity_service.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.phoenixsan.identity.identity_service.exception.ResourceNotFoundException;
import com.phoenixsan.identity.identity_service.master.dto.BranchRequest;
import com.phoenixsan.identity.identity_service.master.dto.BranchResponse;
import com.phoenixsan.identity.identity_service.master.entity.Branch;
import com.phoenixsan.identity.identity_service.master.entity.Organization;
import com.phoenixsan.identity.identity_service.master.repository.BranchRepository;
import com.phoenixsan.identity.identity_service.master.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final OrganizationRepository organizationRepository;

    // ✅ CREATE
    public BranchResponse create(BranchRequest request) {

        // Validate organization
        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Orgnization not found"));

        // Optional: check duplicate code per org
        if (branchRepository.existsByCodeAndOrganizationId(request.getCode(), request.getOrganizationId())) {
            throw new RuntimeException("Branch code already exists for this organization");
        }

        Branch branch = Branch.builder()
                .name(request.getName())
                .code(request.getCode())
                .organization(org)
                .status(request.getStatus())
                .build();

        return mapToResponse(branchRepository.save(branch));
    }

    // ✅ UPDATE
    public BranchResponse update(Long id, BranchRequest request) {

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Orgnization not found"));

        branch.setName(request.getName());
        branch.setCode(request.getCode());
        branch.setOrganization(org);
        branch.setStatus(request.getStatus());

        return mapToResponse(branchRepository.save(branch));
    }

    // ✅ GET BY ID
    public BranchResponse getById(Long id) {

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        return mapToResponse(branch);
    }

    // ✅ GET ALL
    public List<BranchResponse> getAll() {

        return branchRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ DELETE
    public void delete(Long id) {

        if (!branchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Branch not found");
        }

        branchRepository.deleteById(id);
    }

    // 🔁 MAPPER
    private BranchResponse mapToResponse(Branch branch) {

        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .code(branch.getCode())
                .status(branch.getStatus())
                .organizationId(branch.getOrganization().getId())
                .createdAt(branch.getCreatedAt())
                .build();
    }


    public List<BranchResponse> getByOrganizationId(Long organizationId) {

    // Optional validation (recommended)
    if (!organizationRepository.existsById(organizationId)) {
        throw new ResourceNotFoundException("Orgnization not found");
    }

    List<Branch> branches = branchRepository.findByOrganizationId(organizationId);

    return branches.stream()
            .map(this::mapToResponse)
            .toList();
    }
}
