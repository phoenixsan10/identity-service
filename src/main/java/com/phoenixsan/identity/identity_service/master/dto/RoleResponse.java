package com.phoenixsan.identity.identity_service.master.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {
    private Long id;
    private String roleName;
    private String description;
    private String status;
    private Long organizationId;
    private List<RolePermissionResponse> permissions;
    
}
