package com.phoenixsan.identity.identity_service.master.dto;

import lombok.Data;

@Data
public class LinkRequest {
    private String name;
    private String description;
    private String status;
}
