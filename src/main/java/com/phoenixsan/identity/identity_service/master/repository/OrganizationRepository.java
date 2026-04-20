package com.phoenixsan.identity.identity_service.master.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phoenixsan.identity.identity_service.master.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByCode(String code);

    boolean existsByCode(String code);
}
