package com.phoenixsan.identity.identity_service.master.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String userName;
    private String email;
    private String password;

    private Long organizationId;
    private Long branchId;
    private Long departmentId;
    private Long roleId;

    private Boolean canCreate;
    private Boolean canRead;
    private Boolean canUpdate;
    private Boolean canDelete;

    private String status;
}
