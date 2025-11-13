from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from app.core.ollama_client import parse_resume_with_llm
from app.models.resume_schema import ResumeData

router = APIRouter(prefix="/api/ai", tags=["Resume Parser"])

class ResumeParseRequest(BaseModel):
    text: str

@router.post("/parse-resume")
async def parse_resume(req: ResumeParseRequest):
    try:
        parsed = await parse_resume_with_llm(req.text)
        # validate & normalize shape before returning
        validated = ResumeData.model_validate(parsed)
        return {"status": "success", "data": validated.model_dump()}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
