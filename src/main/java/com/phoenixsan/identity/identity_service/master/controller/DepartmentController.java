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

import com.phoenixsan.identity.identity_service.master.dto.DepartmentRequest;
import com.phoenixsan.identity.identity_service.master.dto.DepartmentResponse;
import com.phoenixsan.identity.identity_service.master.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService service;

    // CREATE
    @PostMapping
    public DepartmentResponse create(@RequestBody DepartmentRequest request) {
        return service.create(request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public DepartmentResponse update(@PathVariable Long id,
                                     @RequestBody DepartmentRequest request) {
        return service.update(id, request);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public DepartmentResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // GET ALL
    @GetMapping
    public List<DepartmentResponse> getAll() {
        return service.getAll();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // 🔥 GET BY ORGANIZATION
    @GetMapping("/organization/{organizationId}")
    public List<DepartmentResponse> getByOrganizationId(@PathVariable Long organizationId) {
        return service.getByOrganizationId(organizationId);
    }
}
