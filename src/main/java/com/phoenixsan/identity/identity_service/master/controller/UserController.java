package com.phoenixsan.identity.identity_service.master.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phoenixsan.identity.identity_service.master.dto.UserRequest;
import com.phoenixsan.identity.identity_service.master.dto.UserResponse;
import com.phoenixsan.identity.identity_service.master.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    // CREATE
    @PostMapping
    public UserResponse create(@RequestBody UserRequest request) {
        log.info("Creating new user");
        return service.create(request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id,
                               @RequestBody UserRequest request) {
        log.info("Updating user with ID: {}", id);
        return service.update(id, request);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        return service.getById(id);
    }

    // GET ALL
    @GetMapping
    public List<UserResponse> getAll() {
        log.info("Fetching all users");
        return service.getAll();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        service.delete(id);
    }

    @GetMapping("/organization/{organizationId}")
    public List<UserResponse> getUsersByOrganizationId(@PathVariable Long organizationId) {
        log.info("Fetching users for organization ID: {}", organizationId);
        return service.getUsersByOrganizationId(organizationId);
    }
}
