package com.phoenixsan.identity.identity_service.master.dto;

import lombok.Data;

@Data
public class OrganizationRequest {
    private String name;
    private String code;
    private String status;
}