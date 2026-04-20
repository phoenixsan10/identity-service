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

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService service;

    // CREATE
    @PostMapping
    public BranchResponse create(@RequestBody BranchRequest request) {
        return service.create(request); 
    }

    // UPDATE
    @PutMapping("/{id}")
    public BranchResponse update(@PathVariable Long id,
                                 @RequestBody BranchRequest request) {
        return service.update(id, request);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public BranchResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // GET ALL
    @GetMapping
    public List<BranchResponse> getAll() {
        return service.getAll();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/organization/{organizationId}")
    public List<BranchResponse> getByOrganizationId(@PathVariable Long organizationId) {
        return service.getByOrganizationId(organizationId);
    }
}
