from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from app.core.embedding_model import generate_embedding

router = APIRouter(prefix="/api/ai", tags=["Embeddings"])

class EmbedRequest(BaseModel):
    text: str

@router.post("/embed-text")
async def embed_text(req: EmbedRequest):
    try:
        vector = generate_embedding(req.text)
        return {"status": "success", "embedding": vector}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
