package com.phoenixsan.identity.identity_service.master.dto;

import java.util.List;

import lombok.Data;

@Data
public class RoleRequest {
    private String roleName;
    private String description;
    private Long organizationId;
    private List<RolePermissionRequest> permissions;
}
