package com.phoenixsan.identity.identity_service.master.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;

    private Long organizationId;
    private Long branchId;
    private Long departmentId;
    private Long roleId;

    private Boolean canCreate;
    private Boolean canRead;
    private Boolean canUpdate;
    private Boolean canDelete;

    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
