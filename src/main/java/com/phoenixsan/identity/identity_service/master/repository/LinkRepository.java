package com.phoenixsan.identity.identity_service.master.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phoenixsan.identity.identity_service.master.entity.Link;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByName(String name);

    boolean existsByName(String name);
}
