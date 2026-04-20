package com.phoenixsan.identity.identity_service.security.config;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    // 🔐 Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // // 🔐 Main Security Filter Chain
    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    //     http
    //         .csrf(csrf -> csrf.disable()) // ❗ disable for now (JWT based)

    //         .cors(Customizer.withDefaults()) // ✅ enable CORS

    //         .authorizeHttpRequests(auth -> auth
    //             // 🔓 Public APIs
    //             .requestMatchers(
    //                     "/auth/**",
    //                     "/swagger-ui/**",
    //                     "/v3/api-docs/**",
    //                     "/**"
    //             ).permitAll()

    //             // 🔒 Secure everything else
    //             .anyRequest().authenticated()
    //         );

    //     return http.build();
    // }

    // // 🌐 CORS Configuration (IMPORTANT for cookies)
    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {

    //     CorsConfiguration config = new CorsConfiguration();

    //     // ⚠️ Do NOT use "*" with credentials
    //     config.setAllowedOrigins(List.of("http://localhost:3000"));

    //     config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

    //     config.setAllowedHeaders(List.of("*"));

    //     config.setAllowCredentials(true); // 🔥 VERY IMPORTANT (for cookies)

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", config);

    //     return source;
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**")
                    .permitAll()
                    .anyRequest().authenticated())
            .addFilterBefore(jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
