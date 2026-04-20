package com.phoenixsan.identity.identity_service.master.dto;

import lombok.Data;

@Data
public class RolePermissionRequest {
    private Long linkId;
    private String action; // CREATE, READ, UPDATE, DELETE
}
