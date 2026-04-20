package com.phoenixsan.identity.identity_service.master.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolePermissionResponse {
    private Long linkId;
    private String linkName;
    private String action;
}
