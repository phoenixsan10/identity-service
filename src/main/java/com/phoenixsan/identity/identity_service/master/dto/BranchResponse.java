package com.phoenixsan.identity.identity_service.master.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchResponse {
    private Long id;
    private String name;
    private String code;
    private String status;
    private Long organizationId;
    private LocalDateTime createdAt;
}
