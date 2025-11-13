import json
import re
import ollama
from app.core.prompts import RESUME_PARSER_PROMPT

def _force_json(text: str):
    """Extract a JSON object { ... } from model output."""
    match = re.search(r"\{.*\}", text, re.DOTALL)
    if not match:
        raise ValueError(f"Model did not return JSON.\nRaw: {text}")
    return match.group(0)

async def parse_resume_with_llm(resume_text: str) -> dict:
    prompt = RESUME_PARSER_PROMPT.format(text=resume_text)

    print(">>> Sending request to Ollama...")

    response = ollama.chat(
        model="phi3",
        messages=[
            {"role": "system", "content": "You output strict valid JSON only."},
            {"role": "user", "content": prompt}
        ]
    )

    print(">>> Ollama responded")

    raw = response["message"]["content"]
    print("RAW:", raw)

    try:
        cleaned = _force_json(raw)
        return json.loads(cleaned)
    except Exception:
        return {"raw_output": raw}
