RESUME_PARSER_PROMPT = """
You MUST return ONLY a valid JSON object matching EXACTLY this schema:

{{
  "summary": null,
  "name": null,
  "email": null,
  "phone": null,
  "education": [
    {{
      "institution": null,
      "degree": null,
      "field": null,
      "startDate": null,
      "endDate": null
    }}
  ],
  "experience": [
    {{
      "company": null,
      "position": null,
      "startDate": null,
      "endDate": null,
      "description": null
    }}
  ],
  "skills": []
}}

STRICT RULES:
1. ALL values must be simple strings or null. NO objects. NO arrays inside fields.
2. DO NOT add or rename fields. Use keys exactly as shown.
3. Dates MUST be converted into this exact SQL-safe format: "YYYY-MM-DD".
   - If a month/year is given (e.g. "Jan 2023"), convert to "2023-01-01".
   - If a range is given (e.g. "July 2025 – Present"), extract:
        "startDate": "2025-07-01"
        "endDate": null
   - If the date cannot be interpreted → set the field to null.
4. DO NOT output values like "Present", "Current", "Not specified".
   Use null instead.
5. Each skill MUST be a plain string.
6. ALWAYS return valid JSON, with no trailing commas and no explanations.

Resume text:
{text}
"""
