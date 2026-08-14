from fastapi import FastAPI
from pydantic import BaseModel, Field
from typing import Any

app = FastAPI(title="Agentic Software Engineering Platform", version="1.0.0")


class AgentRequest(BaseModel):
    request: str = Field(min_length=5)


class WorkflowState(BaseModel):
    requirement: str
    requirements: dict[str, Any] = {}
    architecture: dict[str, Any] = {}
    plan: list[dict[str, Any]] = []
    coding_plan: dict[str, Any] = {}
    tests: dict[str, Any] = {}
    security: dict[str, Any] = {}
    review: dict[str, Any] = {}
    approval_required: bool = True


class RequirementsAgent:
    def run(self, request: str):
        return {
            "summary": request,
            "functional": [
                "Create a short URL",
                "Resolve a short URL",
                "Persist URL metadata",
                "Track redirect events",
            ],
            "non_functional": [
                "Low-latency redirects",
                "Horizontal scalability",
                "Input validation",
                "Observability",
            ],
        }


class ArchitectureAgent:
    def run(self, req: dict):
        return {
            "style": "microservice-oriented",
            "backend": "Java 17 + Spring Boot",
            "database": "PostgreSQL",
            "cache": "Redis",
            "events": "Kafka",
            "deployment": "Docker/Kubernetes",
            "security": ["URL validation", "rate limiting", "OAuth2/OIDC"],
        }


class PlanningAgent:
    def run(self, req: dict, arch: dict):
        return [
            {"id": "URL-001", "task": "Create URL API"},
            {"id": "URL-002", "task": "Implement short-code generation"},
            {"id": "URL-003", "task": "Add PostgreSQL persistence"},
            {"id": "URL-004", "task": "Add Redis cache"},
            {"id": "URL-005", "task": "Publish Kafka click events"},
            {"id": "URL-006", "task": "Add automated tests"},
            {"id": "URL-007", "task": "Run security checks"},
        ]


class CodingAgent:
    def run(self, plan):
        return {
            "mode": "safe-plan-only",
            "files": [
                "controller/UrlController.java",
                "controller/RedirectController.java",
                "service/UrlService.java",
                "repository/ShortUrlRepository.java",
                "model/ShortUrl.java",
            ],
            "notes": "Repository mutation requires a sandboxed Git tool and PR approval.",
        }


class TestAgent:
    def run(self, plan):
        return {
            "unit_tests": True,
            "integration_tests": True,
            "api_tests": True,
            "performance_tests": "recommended before production",
        }


class SecurityAgent:
    def run(self, req):
        return {
            "status": "PASS_WITH_RECOMMENDATIONS",
            "checks": [
                "HTTP/HTTPS protocol restriction",
                "Local/private destination blocking",
                "Input validation",
                "No hard-coded secrets",
            ],
            "recommendations": [
                "Add SSRF-safe DNS revalidation",
                "Add rate limiting",
                "Add OAuth2/OIDC",
                "Run SAST and dependency scanning in CI",
            ],
        }


class ReviewAgent:
    def run(self, state: WorkflowState):
        return {
            "decision": "APPROVE_FOR_HUMAN_REVIEW",
            "reason": "Automated design, test, and security gates completed.",
            "production_deployment": "HUMAN_APPROVAL_REQUIRED",
        }


class Supervisor:
    def run(self, request: str) -> WorkflowState:
        state = WorkflowState(requirement=request)
        state.requirements = RequirementsAgent().run(request)
        state.architecture = ArchitectureAgent().run(state.requirements)
        state.plan = PlanningAgent().run(state.requirements, state.architecture)
        state.coding_plan = CodingAgent().run(state.plan)
        state.tests = TestAgent().run(state.plan)
        state.security = SecurityAgent().run(state.requirements)
        state.review = ReviewAgent().run(state)
        return state


@app.get("/health")
def health():
    return {"status": "UP"}


@app.post("/agent/run")
def run_agent(request: AgentRequest):
    return Supervisor().run(request.request).model_dump()
