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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService service;

    // CREATE
    @PostMapping
    public DepartmentResponse create(@RequestBody DepartmentRequest request) {
        log.info("Creating new department");
        return service.create(request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public DepartmentResponse update(@PathVariable Long id,
                                     @RequestBody DepartmentRequest request) {
        log.info("Updating department with ID: {}", id);
        return service.update(id, request);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public DepartmentResponse getById(@PathVariable Long id) {
        log.info("Fetching department with ID: {}", id);
        return service.getById(id);
    }

    // GET ALL
    @GetMapping
    public List<DepartmentResponse> getAll() {
        log.info("Fetching all departments");
        return service.getAll();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting department with ID: {}", id);
        service.delete(id);
    }

    // 🔥 GET BY ORGANIZATION
    @GetMapping("/organization/{organizationId}")
    public List<DepartmentResponse> getByOrganizationId(@PathVariable Long organizationId) {
        log.info("Fetching departments for organization ID: {}", organizationId);
        return service.getByOrganizationId(organizationId);
    }
}
