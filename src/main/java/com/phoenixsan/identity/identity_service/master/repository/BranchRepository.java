package com.phoenixsan.identity.identity_service.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phoenixsan.identity.identity_service.master.entity.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findByOrganizationId(Long organizationId);

    boolean existsByCodeAndOrganizationId(String code, Long organizationId);
}
