package com.phoenixsan.identity.identity_service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.phoenixsan.identity.identity_service.master.entity.User;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private static final String SECRET = "wJgGHBOnrFA2mBvyZahMJZ48/rFMTs3oYFJuBqwFBlk=".trim();

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        System.out.println("Key length: " + keyBytes.length);
        System.out.println("SECRET raw: [" + SECRET + "]");
        // return Keys.hmacShaKeyFor(keyBytes);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user, List<String> linkList,List<String> authorities) {
        
        return Jwts.builder().setHeaderParam("alg", "HS256")
                .setSubject(user.getUserName())
                .claim("userId", user.getId())
                .claim("roleId", user.getRole().getId())
                .claim("organizationId", user.getOrganization().getId())
                .claim("branchId", user.getBranch() != null ? user.getBranch().getId() : null)
                .claim("departmentId", user.getDepartment() != null ? user.getDepartment().getId() : null)

                .claim("canCreate", Boolean.TRUE.equals(user.getCanCreate()))
                .claim("canRead", Boolean.TRUE.equals(user.getCanRead()))
                .claim("canUpdate", Boolean.TRUE.equals(user.getCanUpdate()))
                .claim("canDelete", Boolean.TRUE.equals(user.getCanDelete()))
                .claim("linkList", linkList)
                .claim("authorities", authorities)

                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000L)) // 15 min

                .signWith(getSigningKey(), io.jsonwebtoken.SignatureAlgorithm.HS256) // ← This is the important fix
                .compact();
    }

    // Refresh token remains the same
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserName())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000))
                .signWith(getSigningKey(), io.jsonwebtoken.SignatureAlgorithm.HS256) // ← This is the important fix

                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    public List<String> extractLinkList(String token) {
        Claims claims = extractAllClaims(token);

        return claims.get("linkList", List.class);
    }

   public Boolean extractBooleanClaim(String token, String claimName) {
        return extractAllClaims(token).get(claimName, Boolean.class);
    }

    public List<? extends GrantedAuthority> extractAuthorities(String token){

        Claims claims = extractAllClaims(token);

        List<String> roles = claims.get("authorities", List.class);

        if (roles == null) return Collections.emptyList();

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }


    

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}