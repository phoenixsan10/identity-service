package com.phoenixsan.identity.identity_service.security.auth.service;

import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phoenixsan.identity.identity_service.master.entity.User;
import com.phoenixsan.identity.identity_service.master.repository.UserRepository;
import com.phoenixsan.identity.identity_service.security.auth.dto.LoginRequest;
import com.phoenixsan.identity.identity_service.security.auth.dto.LoginResponse;
import com.phoenixsan.identity.identity_service.security.jwt.JwtService;
import com.phoenixsan.identity.identity_service.security.token.entity.RefreshToken;
import com.phoenixsan.identity.identity_service.security.token.repository.RefreshTokenRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        System.out.println(request.getUserName());
        System.out.println(request.getPassword());
        // 🔍 1. Fetch user
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        System.out.println("/////////////////////////////////////////////");
        // 🔐 2. Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // 🔑 3. Generate Access Token
        String accessToken = jwtService.generateAccessToken(user).trim();
        String[] parts = accessToken.split("\\.");
    if (parts.length != 3) {
        System.out.println("Invalid token structure: " + parts.length + " parts");
        
    }
    
    String header = new String(Base64.getUrlDecoder().decode(parts[0]));
    String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
    
    System.out.println("Header: " + header);
    System.out.println("Payload: " + payload);


        // 🔄 4. Generate Refresh Token
        String refreshTokenValue = jwtService.generateRefreshToken(user);

        // 💾 5. Save Refresh Token in DB
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(refreshTokenValue)
                .expiryDate(LocalDateTime.now().plusDays(7))
                
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        // 🍪 6. Set Refresh Token in HttpOnly Cookie
        Cookie cookie = new Cookie("refreshToken", refreshTokenValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 👉 true in production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

        response.addCookie(cookie);

        // 📤 7. Return Access Token
        return LoginResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
