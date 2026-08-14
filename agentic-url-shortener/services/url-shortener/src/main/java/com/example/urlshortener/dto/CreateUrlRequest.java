package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public record CreateUrlRequest(
    @NotBlank(message = "originalUrl is required")
    String originalUrl,
    Instant expiresAt
) {}
