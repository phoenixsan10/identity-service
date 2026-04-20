package com.phoenixsan.identity.identity_service.master.entity;

import com.phoenixsan.identity.identity_service.master.enums.Action;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role_link_mapping",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"role_id", "link_id", "action"})
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleLinkMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id", nullable = false)
    private Link link;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action action;
}
