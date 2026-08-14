package com.example.urlshortener.repository;

import com.example.urlshortener.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortCodeAndActiveTrue(String shortCode);
    boolean existsByShortCode(String shortCode);
}
