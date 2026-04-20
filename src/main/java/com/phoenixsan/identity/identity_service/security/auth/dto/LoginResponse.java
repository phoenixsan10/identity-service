package com.phoenixsan.identity.identity_service.security.auth.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {
    private String accessToken;
}
