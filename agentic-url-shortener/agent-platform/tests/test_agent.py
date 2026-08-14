from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)


def test_health():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "UP"


def test_agent_workflow():
    response = client.post(
        "/agent/run",
        json={"request": "Add URL expiration and analytics"},
    )
    assert response.status_code == 200
    body = response.json()
    assert body["requirements"]["functional"]
    assert body["architecture"]["database"] == "PostgreSQL"
    assert len(body["plan"]) >= 5
    assert body["security"]["status"] == "PASS_WITH_RECOMMENDATIONS"
    assert body["review"]["decision"] == "APPROVE_FOR_HUMAN_REVIEW"
