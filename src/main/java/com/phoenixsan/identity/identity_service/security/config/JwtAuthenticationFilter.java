package com.phoenixsan.identity.identity_service.security.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import com.phoenixsan.identity.identity_service.security.jwt.JwtService;

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

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            User user = userRepository.findByUserName(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

            // List<RoleLinkMapping> mappings = roleLinkMappingRepository.findByRoleId(user.getRole().getId());
              // ✅ Step 3: Identify API → Link
                String path = request.getRequestURI();
                String method = request.getMethod();
                Action action = Action.valueOf(method);
                System.out.println("METHOD/////////////////////"+method);

            boolean hasAccess = roleLinkMappingRepository.hasAccess(user.getRole().getId(), path, action);
             if (!hasAccess) {
                throw new CustomAccessDeniedException("Access Denied: No Link Permission");
                }

             // ✅ Step 6: User-level permission check
                if (!checkUserPermission(user, action)) {
                throw new CustomAccessDeniedException("Access Denied: User Restriction");
                }
            
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            ); 

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }



    private boolean checkUserPermission(User user, Action action) {
        return switch (action) {
            case CREATE -> Boolean.TRUE.equals(user.getCanCreate());
            case GET -> Boolean.TRUE.equals(user.getCanRead());
            case UPDATE -> Boolean.TRUE.equals(user.getCanUpdate());
            case DELETE -> Boolean.TRUE.equals(user.getCanDelete());
            default -> throw new IllegalArgumentException("Unexpected value: " + action);
        };
    }
}