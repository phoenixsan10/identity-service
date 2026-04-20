package com.phoenixsan.identity.identity_service.security.token.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.phoenixsan.identity.identity_service.master.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 User mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 🔐 Token value
    @Column(nullable = false, unique = true, length = 500)
    private String token;

    // ⏳ Expiry
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // 🚫 Revoked flag
    @Column(nullable = false)
    private Boolean revoked = false;

    // 📅 Audit
    @CreationTimestamp
    private LocalDateTime createdAt;
}
