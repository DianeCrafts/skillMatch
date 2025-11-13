from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routes.parse_resume import router as parse_router

app = FastAPI(title="SkillMatch AI-Service", version="1.0")

# CORS (enable if you’re calling from your swagger or another frontend)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # tighten in prod
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(parse_router)

@app.get("/health")
def health():
    return {"status": "ok"}
