package com.phoenixsan.identity.identity_service.master.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinkResponse {
    private Long id;
    private String name;
    private String description;
    private String status;
}
