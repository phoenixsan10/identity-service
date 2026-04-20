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

import com.phoenixsan.identity.identity_service.master.dto.LinkRequest;
import com.phoenixsan.identity.identity_service.master.dto.LinkResponse;
import com.phoenixsan.identity.identity_service.master.service.LinkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService service;

    // CREATE
    @PostMapping
    public LinkResponse create(@RequestBody LinkRequest request) {
        return service.create(request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public LinkResponse update(@PathVariable Long id,
                               @RequestBody LinkRequest request) {
        return service.update(id, request);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public LinkResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // GET ALL
    @GetMapping
    public List<LinkResponse> getAll() {
        return service.getAll();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
