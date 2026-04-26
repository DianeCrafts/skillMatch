from fastapi import FastAPI, HTTPException, Query
from app.schemas import EmbeddingResponse
from app.embedding_service import EmbeddingService

app = FastAPI(
    title="SkillMatch Embedding Service",
    version="1.0.0",
    description="Generates semantic embeddings for profiles and jobs"
)

embedding_service = EmbeddingService()


@app.get("/health")
def health_check():
    return {"status": "UP"}


@app.post("/api/embeddings", response_model=EmbeddingResponse)
def create_embedding(text: str = Query(...)):
    text = text.strip()

    if not text:
        raise HTTPException(status_code=400, detail="Text must not be empty")

    embedding = embedding_service.generate_embedding(text)
    return EmbeddingResponse(embedding=embedding)