package com.phoenixsan.identity.identity_service.master.dto;

import lombok.Data;

@Data
public class DepartmentRequest {
    private String name;
    private String code;
    private Long organizationId;
    private String status;
}
