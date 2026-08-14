package com.example.urlshortener.controller;

import com.example.urlshortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class RedirectController {
    private final UrlService service;

    public RedirectController(UrlService service) {
        this.service = service;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String destination = service.resolve(shortCode);
        return ResponseEntity.status(302)
                .location(URI.create(destination))
                .build();
    }
}
