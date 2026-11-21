package com.phoenixsan.identity.identity_service.master.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.phoenixsan.identity.identity_service.exception.ResourceNotFoundException;
import com.phoenixsan.identity.identity_service.master.dto.LinkRequest;
import com.phoenixsan.identity.identity_service.master.dto.LinkResponse;
import com.phoenixsan.identity.identity_service.master.entity.Link;
import com.phoenixsan.identity.identity_service.master.repository.LinkRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkService {

    private final LinkRepository linkRepository;

    // ✅ CREATE
    public LinkResponse create(LinkRequest request) {
        log.info("Creating new link with name: {}", request.getName());

        if (linkRepository.existsByName(request.getName())) {
            log.warn("Link with name {} already exists", request.getName());
            throw new RuntimeException("Link already exists");
        }

        Link link = Link.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();

        return mapToResponse(linkRepository.save(link));
    }

    // ✅ UPDATE
    public LinkResponse update(Long id, LinkRequest request) {
        log.info("Updating link with ID: {}", id);

        Link link = linkRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Link not found"));


        link.setName(request.getName());
        link.setDescription(request.getDescription());
        link.setStatus(request.getStatus());

        return mapToResponse(linkRepository.save(link));
    }

    // ✅ GET BY ID
    public LinkResponse getById(Long id) {
        log.info("Fetching link with ID: {}", id);

        Link link = linkRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Link not found"));


        return mapToResponse(link);
    }

    // ✅ GET ALL
    public List<LinkResponse> getAll() {
        log.info("Fetching all links");

        return linkRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ DELETE
    public void delete(Long id) {
        log.info("Deleting link with ID: {}", id);

        if (!linkRepository.existsById(id)) {
            log.warn("Link with ID: {} not found for deletion", id);
                throw new ResourceNotFoundException("Orgnization not found");

        }

        linkRepository.deleteById(id);
    }

    // 🔁 MAPPER
    private LinkResponse mapToResponse(Link link) {

        return LinkResponse.builder()
                .id(link.getId())
                .name(link.getName())
                .description(link.getDescription())
                .status(link.getStatus())
                .build();
    }
}
