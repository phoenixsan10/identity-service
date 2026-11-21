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

import com.phoenixsan.identity.identity_service.master.dto.BranchRequest;
import com.phoenixsan.identity.identity_service.master.dto.BranchResponse;
import com.phoenixsan.identity.identity_service.master.service.BranchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
@Slf4j
public class BranchController {

    private final BranchService service;

    // CREATE
    @PostMapping
    public BranchResponse create(@RequestBody BranchRequest request) {
        log.info("Creating new branch");
        return service.create(request); 
    }

    // UPDATE
    @PutMapping("/{id}")
    public BranchResponse update(@PathVariable Long id,
                                 @RequestBody BranchRequest request) {
        log.info("Updating branch with ID: {}", id);
        return service.update(id, request);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public BranchResponse getById(@PathVariable Long id) {
        log.info("Fetching branch with ID: {}", id);
        return service.getById(id);
    }

    // GET ALL
    @GetMapping
    public List<BranchResponse> getAll() {
        log.info("Fetching all branches");
        return service.getAll();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting branch with ID: {}", id);
        service.delete(id);
    }

    @GetMapping("/organization/{organizationId}")
    public List<BranchResponse> getByOrganizationId(@PathVariable Long organizationId) {
        log.info("Fetching branches for organization ID: {}", organizationId);
        return service.getByOrganizationId(organizationId);
    }
}
