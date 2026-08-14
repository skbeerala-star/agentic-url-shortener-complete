# Agentic URL Shortener — Runnable Reference Implementation

A production-oriented reference implementation containing:

- Java 17 + Spring Boot URL shortener
- PostgreSQL + Flyway
- Redis caching
- Kafka click events
- Python/FastAPI agent orchestrator with deterministic workflow
- Docker Compose
- JUnit tests
- Python tests
- GitHub Actions CI
- Prometheus metrics endpoint
- Security validation for submitted URLs
- Human approval gate for deployment decisions

## Prerequisites

- Docker Desktop
- Docker Compose
- Java 17+ (only needed for local Maven runs)
- Python 3.11+ (only needed for local agent runs)

## Run everything

```bash
docker compose up --build
```

Services:

- URL API: http://localhost:8080
- Agent API: http://localhost:8000
- PostgreSQL: localhost:5432
- Redis: localhost:6379
- Kafka: localhost:9092

## Create a short URL

```bash
curl -X POST http://localhost:8080/api/v1/urls \
  -H 'Content-Type: application/json' \
  -d '{"originalUrl":"https://example.com/products/123","expiresAt":null}'
```

Example response:

```json
{
  "shortCode": "aB12xY",
  "shortUrl": "http://localhost:8080/aB12xY"
}
```

Then:

```bash
curl -i http://localhost:8080/aB12xY
```

## Run the agent workflow

```bash
curl -X POST http://localhost:8000/agent/run \
  -H 'Content-Type: application/json' \
  -d '{"request":"Add URL expiration and analytics"}'
```

The supervisor executes:

requirements -> architecture -> planning -> coding plan -> tests -> security -> review -> approval recommendation.

This reference implementation deliberately makes the "coding agent" produce a structured implementation plan instead of modifying arbitrary files. A real enterprise version should put repository mutation behind Git branches, pull requests, sandboxed tools, policy checks, and human approval.

## Local Java tests

```bash
cd services/url-shortener
mvn test
```

## Local agent tests

```bash
cd agent-platform
python -m pytest
```

## Production hardening checklist

- Replace demo Kafka configuration with managed Kafka/MSK.
- Add OAuth2/OIDC and role-based authorization.
- Put the API behind WAF/API Gateway.
- Add rate limiting.
- Add distributed tracing and centralized logs.
- Use secrets manager rather than environment-file secrets.
- Add SAST, dependency, container and IaC scanning.
- Use a cryptographically secure short-code generator or distributed ID strategy.
- Add an outbox pattern if click-event delivery must be guaranteed.
- Use Kubernetes/EKS and Terraform modules for production infrastructure.
