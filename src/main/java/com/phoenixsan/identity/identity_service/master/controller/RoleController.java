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

import com.phoenixsan.identity.identity_service.master.dto.RoleRequest;
import com.phoenixsan.identity.identity_service.master.dto.RoleResponse;
import com.phoenixsan.identity.identity_service.master.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    // CREATE
    @PostMapping
    public RoleResponse create(@RequestBody RoleRequest request) {
        return service.create(request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public RoleResponse update(@PathVariable Long id,
                               @RequestBody RoleRequest request) {
        return service.update(id, request);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public RoleResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // GET ALL
    @GetMapping
    public List<RoleResponse> getAll() {
        return service.getAll();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/organization/{organizationId}")
    public List<RoleResponse> getRolesByOrganizationId(@PathVariable Long organizationId) {
        return service.getRolesByOrganizationId(organizationId);
    }
}