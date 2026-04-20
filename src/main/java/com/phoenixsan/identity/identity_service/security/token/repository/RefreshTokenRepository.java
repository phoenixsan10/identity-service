package com.phoenixsan.identity.identity_service.security.token.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phoenixsan.identity.identity_service.security.token.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
