package com.phoenixsan.identity.identity_service.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.phoenixsan.identity.identity_service.exception.ResourceNotFoundException;
import com.phoenixsan.identity.identity_service.master.dto.UserRequest;
import com.phoenixsan.identity.identity_service.master.dto.UserResponse;
import com.phoenixsan.identity.identity_service.master.entity.Branch;
import com.phoenixsan.identity.identity_service.master.entity.Department;
import com.phoenixsan.identity.identity_service.master.entity.Organization;
import com.phoenixsan.identity.identity_service.master.entity.Role;
import com.phoenixsan.identity.identity_service.master.entity.User;
import com.phoenixsan.identity.identity_service.master.repository.BranchRepository;
import com.phoenixsan.identity.identity_service.master.repository.DepartmentRepository;
import com.phoenixsan.identity.identity_service.master.repository.OrganizationRepository;
import com.phoenixsan.identity.identity_service.master.repository.RoleRepository;
import com.phoenixsan.identity.identity_service.master.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final BranchRepository branchRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;

    // ✅ CREATE
    public UserResponse create(UserRequest request) {
        log.info("Creating new user with username: {}", request.getUserName());

        if (userRepository.existsByUserName(request.getUserName())) {
            log.warn("Username {} already exists", request.getUserName());
            throw new ResourceNotFoundException("Username already exists");
        }

        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        Branch branch = request.getBranchId() != null
                ? branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found"))
                : null;

        Department dept = request.getDepartmentId() != null
                ? departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"))
                : null;

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(request.getPassword()) // 🔐 later encrypt
                .organization(org)
                .branch(branch)
                .department(dept)
                .role(role)
                .canCreate(Boolean.TRUE.equals(request.getCanCreate()))
                .canRead(Boolean.TRUE.equals(request.getCanRead()))
                .canUpdate(Boolean.TRUE.equals(request.getCanUpdate()))
                .canDelete(Boolean.TRUE.equals(request.getCanDelete()))
                .status(request.getStatus())
                .build();

        return mapToResponse(userRepository.save(user));
    }

    // ✅ UPDATE
    public UserResponse update(Long id, UserRequest request) {
        log.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        Branch branch = request.getBranchId() != null
                ? branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found"))
                : null;

        Department dept = request.getDepartmentId() != null
                ? departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"))
                : null;

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // later encrypt
        user.setOrganization(org);
        user.setBranch(branch);
        user.setDepartment(dept);
        user.setRole(role);
        user.setCanCreate(Boolean.TRUE.equals(request.getCanCreate()));
        user.setCanRead(Boolean.TRUE.equals(request.getCanRead()));
        user.setCanUpdate(Boolean.TRUE.equals(request.getCanUpdate()));
        user.setCanDelete(Boolean.TRUE.equals(request.getCanDelete()));
        user.setStatus(request.getStatus());

        return mapToResponse(userRepository.save(user));
    }

    // ✅ GET BY ID
    public UserResponse getById(Long id) {
        log.info("Fetching user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToResponse(user);
    }

    // ✅ GET ALL
    public List<UserResponse> getAll() {
        log.info("Fetching all users");

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ DELETE
    public void delete(Long id) {
        log.info("Deleting user with ID: {}", id);

        if (!userRepository.existsById(id)) {
            log.warn("User with ID: {} not found for deletion", id);
            throw new ResourceNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }

    public List<UserResponse> getUsersByOrganizationId(Long organizationId) {
        log.info("Fetching users for organization ID: {}", organizationId);

        if (!organizationRepository.existsById(organizationId)) {
            log.warn("Organization with ID: {} not found", organizationId);
            throw new ResourceNotFoundException("Organization not found");
        }

        return userRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔁 MAPPER
    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUserName())
                .email(user.getEmail())
                .organizationId(user.getOrganization().getId())
                .branchId(user.getBranch() != null ? user.getBranch().getId() : null)
                .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                .roleId(user.getRole().getId())
                .canCreate(user.getCanCreate())
                .canRead(user.getCanRead())
                .canUpdate(user.getCanUpdate())
                .canDelete(user.getCanDelete())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
