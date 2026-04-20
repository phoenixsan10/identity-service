package com.phoenixsan.identity.identity_service.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phoenixsan.identity.identity_service.master.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleNameAndOrganizationId(String roleName, Long organizationId);

    boolean existsByRoleNameAndOrganizationId(String roleName, Long organizationId);

    List<Role> findByOrganizationId(Long organizationId);
}
