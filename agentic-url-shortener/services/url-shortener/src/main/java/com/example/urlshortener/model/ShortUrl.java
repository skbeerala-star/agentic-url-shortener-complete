package com.example.urlshortener.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "short_urls")
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_code", nullable = false, unique = true, length = 16)
    private String shortCode;

    @Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean active = true;

    public Long getId() { return id; }
    public String getShortCode() { return shortCode; }
    public void setShortCode(String v) { shortCode = v; }
    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String v) { originalUrl = v; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant v) { createdAt = v; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant v) { expiresAt = v; }
    public boolean isActive() { return active; }
    public void setActive(boolean v) { active = v; }
}
