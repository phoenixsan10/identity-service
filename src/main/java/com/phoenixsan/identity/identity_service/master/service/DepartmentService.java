package com.phoenixsan.identity.identity_service.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.phoenixsan.identity.identity_service.exception.ResourceNotFoundException;
import com.phoenixsan.identity.identity_service.master.dto.DepartmentRequest;
import com.phoenixsan.identity.identity_service.master.dto.DepartmentResponse;
import com.phoenixsan.identity.identity_service.master.entity.Department;
import com.phoenixsan.identity.identity_service.master.entity.Organization;
import com.phoenixsan.identity.identity_service.master.repository.DepartmentRepository;
import com.phoenixsan.identity.identity_service.master.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final OrganizationRepository organizationRepository;

    // ✅ CREATE
    public DepartmentResponse create(DepartmentRequest request) {

        Organization org = organizationRepository.findById(request.getOrganizationId())
               .orElseThrow(() -> new ResourceNotFoundException("Orgnization not found"));

        if (departmentRepository.existsByCodeAndOrganizationId(
                request.getCode(), request.getOrganizationId())) {
            throw new RuntimeException("Department code already exists for this organization");
        }

        Department dept = Department.builder()
                .name(request.getName())
                .code(request.getCode())
                .organization(org)
                .status(request.getStatus())
                .build();

        return mapToResponse(departmentRepository.save(dept));
    }

    // ✅ UPDATE
    public DepartmentResponse update(Long id, DepartmentRequest request) {

        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Orgnization not found"));

        dept.setName(request.getName());
        dept.setCode(request.getCode());
        dept.setOrganization(org);
        dept.setStatus(request.getStatus());

        return mapToResponse(departmentRepository.save(dept));
    }

    // ✅ GET BY ID
    public DepartmentResponse getById(Long id) {

        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        return mapToResponse(dept);
    }

    // ✅ GET ALL
    public List<DepartmentResponse> getAll() {

        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ DELETE
    public void delete(Long id) {

        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found");
        }

        departmentRepository.deleteById(id);
    }

    // 🔥 GET BY ORGANIZATION ID
    public List<DepartmentResponse> getByOrganizationId(Long organizationId) {

        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException("Orgnization not found");

        }

        return departmentRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔁 MAPPER
    private DepartmentResponse mapToResponse(Department dept) {

        return DepartmentResponse.builder()
                .id(dept.getId())
                .name(dept.getName())
                .code(dept.getCode())
                .status(dept.getStatus())
                .organizationId(dept.getOrganization().getId())
                .createdAt(dept.getCreatedAt())
                .build();
    }
}
