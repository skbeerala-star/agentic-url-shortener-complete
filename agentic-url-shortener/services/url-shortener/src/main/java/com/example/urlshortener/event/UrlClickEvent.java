package com.example.urlshortener.event;

import java.time.Instant;

public record UrlClickEvent(
    String shortCode,
    Instant timestamp
) {}
