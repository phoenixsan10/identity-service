package com.phoenixsan.identity.identity_service.security.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
}
