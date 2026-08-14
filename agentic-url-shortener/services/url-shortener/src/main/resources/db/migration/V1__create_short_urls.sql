CREATE TABLE short_urls (
    id BIGSERIAL PRIMARY KEY,
    short_code VARCHAR(16) NOT NULL UNIQUE,
    original_url TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    expires_at TIMESTAMPTZ NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_short_urls_active_code
ON short_urls(short_code, active);
