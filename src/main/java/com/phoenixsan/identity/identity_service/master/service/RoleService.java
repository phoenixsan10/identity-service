package com.phoenixsan.identity.identity_service.master.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.phoenixsan.identity.identity_service.exception.ResourceNotFoundException;
import com.phoenixsan.identity.identity_service.master.dto.RolePermissionRequest;
import com.phoenixsan.identity.identity_service.master.dto.RolePermissionResponse;
import com.phoenixsan.identity.identity_service.master.dto.RoleRequest;
import com.phoenixsan.identity.identity_service.master.dto.RoleResponse;
import com.phoenixsan.identity.identity_service.master.entity.Link;
import com.phoenixsan.identity.identity_service.master.entity.Organization;
import com.phoenixsan.identity.identity_service.master.entity.Role;
import com.phoenixsan.identity.identity_service.master.entity.RoleLinkMapping;
import com.phoenixsan.identity.identity_service.master.enums.Action;
import com.phoenixsan.identity.identity_service.master.repository.LinkRepository;
import com.phoenixsan.identity.identity_service.master.repository.OrganizationRepository;
import com.phoenixsan.identity.identity_service.master.repository.RoleLinkMappingRepository;
import com.phoenixsan.identity.identity_service.master.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final LinkRepository linkRepository;
    private final RoleLinkMappingRepository mappingRepository;

    // ✅ CREATE ROLE + PERMISSIONS
    public RoleResponse create(RoleRequest request) {

        Organization org = organizationRepository.findById(request.getOrganizationId())
                        .orElseThrow(() -> new ResourceNotFoundException("Orgnization not found"));


        // Optional: duplicate role check
        if (roleRepository.existsByRoleNameAndOrganizationId(
                request.getRoleName(), request.getOrganizationId())) {
            throw new RuntimeException("Role already exists for this organization");
        }

        Role role = Role.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .organization(org)
                .status("ACTIVE")
                .build();

        Role savedRole = roleRepository.save(role);

        // 🔥 Save mappings
        List<RolePermissionResponse> permissions = new ArrayList<>();

        for (RolePermissionRequest perm : request.getPermissions()) {

            Link link = linkRepository.findById(perm.getLinkId())
                    .orElseThrow(() -> new RuntimeException("Link not found"));

            Action action = Action.valueOf(perm.getAction());

            RoleLinkMapping mapping = RoleLinkMapping.builder()
                    .role(savedRole)
                    .link(link)
                    .action(action)
                    .build();

            mappingRepository.save(mapping);

            permissions.add(RolePermissionResponse.builder()
                    .linkId(link.getId())
                    .linkName(link.getName())
                    .action(action.name())
                    .build());
        }

        return mapToResponse(savedRole, permissions);
    }

    // ✅ UPDATE ROLE + RESET PERMISSIONS
    public RoleResponse update(Long id, RoleRequest request) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));


        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Orgnization not found"));


        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setOrganization(org);

        Role updatedRole = roleRepository.save(role);

        // 🔥 Delete old mappings
        mappingRepository.deleteByRoleId(id);

        // 🔥 Add new mappings
        List<RolePermissionResponse> permissions = new ArrayList<>();

        for (RolePermissionRequest perm : request.getPermissions()) {

            Link link = linkRepository.findById(perm.getLinkId())
                    .orElseThrow(() -> new ResourceNotFoundException("Link not found"));

            com.phoenixsan.identity.identity_service.master.enums.Action action = Action.valueOf(perm.getAction());

            RoleLinkMapping mapping = RoleLinkMapping.builder()
                    .role(updatedRole)
                    .link(link)
                    .action(action)
                    .build();

            mappingRepository.save(mapping);

            permissions.add(RolePermissionResponse.builder()
                    .linkId(link.getId())
                    .linkName(link.getName())
                    .action(action.name())
                    .build());
        }

        return mapToResponse(updatedRole, permissions);
    }

    // ✅ GET BY ID
    public RoleResponse getById(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));


        List<RolePermissionResponse> permissions =
                mappingRepository.findByRoleId(id)
                        .stream()
                        .map(m -> RolePermissionResponse.builder()
                                .linkId(m.getLink().getId())
                                .linkName(m.getLink().getName())
                                .action(m.getAction().name())
                                .build())
                        .toList();

        return mapToResponse(role, permissions);
    }

    // ✅ GET ALL
    public List<RoleResponse> getAll() {

        return roleRepository.findAll()
                .stream()
                .map(role -> {
                    List<RolePermissionResponse> permissions =
                            mappingRepository.findByRoleId(role.getId())
                                    .stream()
                                    .map(m -> RolePermissionResponse.builder()
                                            .linkId(m.getLink().getId())
                                            .linkName(m.getLink().getName())
                                            .action(m.getAction().name())
                                            .build())
                                    .toList();

                    return mapToResponse(role, permissions);
                })
                .toList();
    }

    // ✅ DELETE
    public void delete(Long id) {

        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found");
        }

        mappingRepository.deleteByRoleId(id);
        roleRepository.deleteById(id);
    }

    public List<RoleResponse> getRolesByOrganizationId(Long organizationId) {

    if (!organizationRepository.existsById(organizationId)) {
        throw new ResourceNotFoundException("Organization not found");
    }

    return roleRepository.findByOrganizationId(organizationId)
            .stream()
            .map(role -> {

                List<RolePermissionResponse> permissions =
                        mappingRepository.findByRoleId(role.getId())
                                .stream()
                                .map(m -> RolePermissionResponse.builder()
                                        .linkId(m.getLink().getId())
                                        .linkName(m.getLink().getName())
                                        .action(m.getAction().name())
                                        .build())
                                .toList();

                return mapToResponse(role, permissions);
            })
            .toList();
}

    // 🔁 MAPPER
    private RoleResponse mapToResponse(Role role,
                                       List<RolePermissionResponse> permissions) {

        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .status(role.getStatus())
                .organizationId(role.getOrganization().getId())
                .permissions(permissions)
                .build();
    }
}
