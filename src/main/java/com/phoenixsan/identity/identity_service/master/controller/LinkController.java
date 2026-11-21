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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
@Slf4j
public class LinkController {

    private final LinkService service;

    // CREATE
    @PostMapping
    public LinkResponse create(@RequestBody LinkRequest request) {
        log.info("Creating new link");
        return service.create(request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public LinkResponse update(@PathVariable Long id,
                               @RequestBody LinkRequest request) {
        log.info("Updating link with ID: {}", id);
        return service.update(id, request);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public LinkResponse getById(@PathVariable Long id) {
        log.info("Fetching link with ID: {}", id);
        return service.getById(id);
    }

    // GET ALL
    @GetMapping
    public List<LinkResponse> getAll() {
        log.info("Fetching all links");
        return service.getAll();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting link with ID: {}", id);
        service.delete(id);
    }
}
