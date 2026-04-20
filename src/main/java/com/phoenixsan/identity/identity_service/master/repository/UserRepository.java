package com.phoenixsan.identity.identity_service.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phoenixsan.identity.identity_service.master.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    boolean existsByUserName(String userName);

    List<User> findByOrganizationId(Long organizationId);
}
