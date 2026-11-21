package com.phoenixsan.identity.identity_service.security.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.phoenixsan.identity.identity_service.exception.CustomAccessDeniedException;
import com.phoenixsan.identity.identity_service.master.entity.User;
import com.phoenixsan.identity.identity_service.master.enums.Action;
import com.phoenixsan.identity.identity_service.master.repository.RoleLinkMappingRepository;
import com.phoenixsan.identity.identity_service.master.repository.UserRepository;
import com.phoenixsan.identity.identity_service.redis.service.RedisService;
import com.phoenixsan.identity.identity_service.security.jwt.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RoleLinkMappingRepository roleLinkMappingRepository;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        System.out.println("AUTH HEADER: " + authHeader);

        // ✅ Step 1: Extract token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            // ✅ Step 2: Validate FIRST (VERY IMPORTANT)
            if (jwtService.isTokenValid(token)) {
                username = jwtService.extractUsername(token);
            } else {
                throw new CustomAccessDeniedException("Invalid JWT Token");
            }
        }

        System.out.println("TOKEN: " + token);
        System.out.println("USERNAME: " + username);

        // 🔥 Fetch actual user (IMPORTANT)

        // ✅ Step 3: Authenticate user
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            
            // ✅ Step 4: Identify API → Link
            Long userId = jwtService.extractUserId(token);
            String redisToken = redisService.getToken(userId);
            String path = request.getRequestURI();
            String method = request.getMethod();
            Action action = Action.valueOf(method);
            System.out.println("METHOD/////////////////////" + method);

            System.out.println(redisToken);
            if(!token.equals(redisToken)){
                throw new CustomAccessDeniedException("Invalid JWT Token");
            }

            List<String> linkList = jwtService.extractLinkList(redisToken);
            System.out.println(linkList);
             if (!linkList.contains(path)) {
                 throw new CustomAccessDeniedException("Access Denied: No Link Permission");
             }

            // ✅ Step 6: User-level permission check
            Boolean canCreate = jwtService.extractBooleanClaim(token, "canCreate");
            Boolean canRead   = jwtService.extractBooleanClaim(token, "canRead");
            Boolean canUpdate = jwtService.extractBooleanClaim(token, "canUpdate");
            Boolean canDelete = jwtService.extractBooleanClaim(token, "canDelete");
            if (!checkPermission(action, canCreate, canRead, canUpdate, canDelete)) {
                throw new CustomAccessDeniedException("Access Denied");
            }

            List<? extends GrantedAuthority>  authorities =jwtService.extractAuthorities(token);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities);

            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

   private boolean checkPermission(Action action, Boolean canCreate, Boolean canRead, Boolean canUpdate, Boolean canDelete) {
    return switch (action) {
        case CREATE -> Boolean.TRUE.equals(canCreate);
        case GET -> Boolean.TRUE.equals(canRead);
        case UPDATE -> Boolean.TRUE.equals(canUpdate);
        case DELETE -> Boolean.TRUE.equals(canDelete);
        default -> false;
    };
}
}