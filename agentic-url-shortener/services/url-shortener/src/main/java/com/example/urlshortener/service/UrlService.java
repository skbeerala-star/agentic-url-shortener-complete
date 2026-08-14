package com.example.urlshortener.service;

import com.example.urlshortener.event.UrlClickEvent;
import com.example.urlshortener.model.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import com.example.urlshortener.security.UrlValidator;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class UrlService {
    private static final String PREFIX = "short-url:";
    private final ShortUrlRepository repository;

    @Resource(name = "redisTemplate")
    private final RedisTemplate<String, String> redis;
    
    private final KafkaTemplate<String, UrlClickEvent> kafka;
    private final UrlValidator validator;
    private final SecureRandom random = new SecureRandom();
    private final long cacheTtl;

    public UrlService(
            ShortUrlRepository repository,
            RedisTemplate<String, String> redis,
            KafkaTemplate<String, UrlClickEvent> kafka,
            UrlValidator validator,
            @Value("${app.cache-ttl-seconds:3600}") long cacheTtl) {
        this.repository = repository;
        this.redis = redis;
        this.kafka = kafka;
        this.validator = validator;
        this.cacheTtl = cacheTtl;
    }

    @Transactional
    public ShortUrl create(String originalUrl, Instant expiresAt) {
        validator.validate(originalUrl);
        if (expiresAt != null && !expiresAt.isAfter(Instant.now())) {
            throw new IllegalArgumentException("expiresAt must be in the future");
        }

        String code;
        do {
            code = randomCode(7);
        } while (repository.existsByShortCode(code));

        ShortUrl entity = new ShortUrl();
        entity.setShortCode(code);
        entity.setOriginalUrl(originalUrl);
        entity.setCreatedAt(Instant.now());
        entity.setExpiresAt(expiresAt);
        entity.setActive(true);

        ShortUrl saved = repository.save(entity);
        putCache(saved);
        return saved;
    }

    public String resolve(String code) {
        String key = PREFIX + code;
        String cached = redis.opsForValue().get(key);
        if (cached != null) {
            publishClick(code);
            return cached;
        }

        ShortUrl entity = repository.findByShortCodeAndActiveTrue(code)
                .orElseThrow(() -> new IllegalArgumentException("Short URL not found"));

        if (entity.getExpiresAt() != null && !entity.getExpiresAt().isAfter(Instant.now())) {
            entity.setActive(false);
            repository.save(entity);
            redis.delete(key);
            throw new IllegalArgumentException("Short URL expired");
        }

        putCache(entity);
        publishClick(code);
        return entity.getOriginalUrl();
    }

    private void putCache(ShortUrl entity) {
        Duration ttl = Duration.ofSeconds(cacheTtl);
        if (entity.getExpiresAt() != null) {
            ttl = Duration.between(Instant.now(), entity.getExpiresAt());
            if (ttl.isNegative() || ttl.isZero()) return;
        }
        redis.opsForValue().set(PREFIX + entity.getShortCode(), entity.getOriginalUrl(), ttl);
    }

    private void publishClick(String code) {
        kafka.send("url-clicks", code,
                new UrlClickEvent(code, Instant.now()));
    }

    private String randomCode(int length) {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder b = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            b.append(chars.charAt(random.nextInt(chars.length())));
        }
        return b.toString();
    }
}
