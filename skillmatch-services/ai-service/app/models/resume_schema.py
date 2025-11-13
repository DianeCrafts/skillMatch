from pydantic import BaseModel
from typing import List, Optional

class Education(BaseModel):
    institution: Optional[str] = None
    degree: Optional[str] = None
    field: Optional[str] = None
    startDate: Optional[str] = None
    endDate: Optional[str] = None

class Experience(BaseModel):
    company: Optional[str] = None
    position: Optional[str] = None
    startDate: Optional[str] = None
    endDate: Optional[str] = None
    description: Optional[str] = None

class ResumeData(BaseModel):
    summary: Optional[str] = None
    name: Optional[str] = None
    email: Optional[str] = None
    phone: Optional[str] = None
    education: List[Education] = []
    experience: List[Experience] = []
    skills: List[str] = []
