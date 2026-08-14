package com.example.urlshortener.controller;

import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.dto.CreateUrlResponse;
import com.example.urlshortener.model.ShortUrl;
import com.example.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {
    private final UrlService service;
    private final String baseUrl;

    public UrlController(UrlService service, @Value("${app.base-url}") String baseUrl) {
        this.service = service;
        this.baseUrl = baseUrl;
    }

    @PostMapping
    public ResponseEntity<CreateUrlResponse> create(
            @Valid @RequestBody CreateUrlRequest request) {
        ShortUrl saved = service.create(request.originalUrl(), request.expiresAt());
        return ResponseEntity.status(201).body(
                new CreateUrlResponse(
                        saved.getShortCode(),
                        baseUrl + "/" + saved.getShortCode()));
    }

    @GetMapping("/{shortCode}/destination")
    public ResponseEntity<String> destination(@PathVariable String shortCode) {
        return ResponseEntity.ok(service.resolve(shortCode));
    }
}
