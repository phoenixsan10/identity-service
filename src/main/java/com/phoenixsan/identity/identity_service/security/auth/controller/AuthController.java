package com.phoenixsan.identity.identity_service.security.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phoenixsan.identity.identity_service.security.auth.dto.LoginRequest;
import com.phoenixsan.identity.identity_service.security.auth.dto.LoginResponse;
import com.phoenixsan.identity.identity_service.security.auth.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request,
                               HttpServletResponse response) {
         LoginResponse TOKEN = authService.login(request, response);
         return TOKEN;
    }
}
