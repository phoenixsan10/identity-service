package com.phoenixsan.identity.identity_service.security.auth.service;


import com.phoenixsan.identity.identity_service.master.entity.User;
import com.phoenixsan.identity.identity_service.master.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                new ArrayList<>() // 🔥 no roles yet (RBAC will come later)
        );
    }
}
