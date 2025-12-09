# SkillMatch – AI-Powered Resume & Job Matching Platform

SkillMatch is a full-stack, AI-driven job-matching platform built with a modern microservices architecture.
It helps job seekers find the most relevant job opportunities by analyzing their resumes using an LLM (Phi-3) and matching them with job postings using semantic embeddings and vector search.
Recruiters can post jobs, review applicants, and manage applications, while users can upload resumes, browse jobs, save jobs, and receive personalized recommendations.
SkillMatch processes resumes, interprets job postings, and uses machine learning to match candidates and jobs based on real skills and experience.

The system combines Spring Boot, FastAPI, LLMs, Elasticsearch, and PostgreSQL to deliver a complete end-to-end recruitment solution.

The demo video is available below on YouTube:👇

<p align="center">
  <a href="https://www.youtube.com/watch?v=zogT5CvejKM">
    <img src="https://img.youtube.com/vi/zogT5CvejKM/maxresdefault.jpg" alt="Demo Video" width="50%">
  </a>
</p>


##  System Architecture Overview
<img width="1016" height="603" alt="image" src="https://github.com/user-attachments/assets/15cfa6c0-2539-4e3e-8312-7bed585ed115" />


###  Layers Overview

| **Layer** | **Technology** | **Purpose** |
|------------|----------------|--------------|
| **Frontend** | Vue.js 3 + Vite | User & recruiter UI for job browsing, resume upload, applications, and recommendations |
| **Backend Gateway** | Java 21 + Spring Boot | User Service, Resume Service, Job Service — core business logic and API endpoints|
| **AI Layer** | Python + FastAPI + Ollama (Phi-3) + SentenceTransformers | Resume parsing (LLM), embedding generation, AI-powered job matching |
| **Search Engine** | Elasticsearch (Full-text + KNN vector search) | Keyword search, semantic search, recommendation engine |
| **Databases** | PostgreSQL | Persistent storage for users, jobs, resumes, applications, saved jobs |
| **Authentication** | JWT + Spring Security | Stateless authentication, RBAC, secure inter-service communication |
| **File Processing** | Apache Tika | Extracting text from uploaded PDFs/DOCX resumes |
| **Containerization** | Docker + Docker Compose | Local development, service orchestration, multi-container setup |
| **Auth** | JWT + Spring Security | Secure authentication & authorization |

---
SkillMatch is composed of four microservices, each with a clear responsibility:
##  Key Microservices

###  UserService
The User Service is a core component of the SkillMatch platform responsible for user identity management, authentication, authorization, and secure session handling.
It provides REST APIs for registration, login, profile management, and role-based access control, serving as the security gateway for the entire system.

This service ensures that all interactions—whether from job seekers, recruiters, or administrators—are authenticated and properly authorized before accessing other microservices.

#### ⚙️Key Responsibilities:
- User registration with encrypted passwords
- Email & password authentication
- JWT generation with role claims
- Stateless authentication across microservices
- Role-based access (ADMIN / USER / RECRUITER) via Spring Security
- Secure CORS policy for frontend communication
- API documentation through OpenAPI/Swagger
- User CRUD operations for administrative purposes

###  JobService
The Job Service is a central microservice in the SkillMatch platform responsible for job creation, job search, job recommendations, job applications, and saved job management.
It provides REST APIs for recruiters to manage job postings and for users to browse, save, apply, and receive personalized job recommendations.

This service connects three major components of the system:
job postings → user resumes → AI-powered job matching.

#### ⚙️ Key Responsibilities
- Job Creation, Editing & Management (Recruiters)
  - Recruiters can create, update, and delete job postings
  - Stores job details such as title, description, skills, experience, salary, and location
  - Automatically generates semantic embeddings for each job using the AI Service
  - Saves job postings to both the database and Elasticsearch index

- Supports two types of search:

  - 🔍 Keyword Search
  Uses Elasticsearch: to search across fields like, title ,description, requirements, ... for enabling filtering feature

  - 🧠 Vector Search (AI-Based Recommendations)
    -  Fetches the user’s resume embedding
    -  Performs KNN vector search in Elasticsearch
    -  Returns top jobs that best match the user's profile

This is what powers your personalized job recommendations.


- Job Applications

  - Users can apply to a job through the ApplicationService
  - Fetches the user's resume via ResumeService
  - Validates that resume contains skills
  - Calculates a simple skill match score
  - Stores application data in the database
  - Allows recruiters to view all applicants for their job postings
  - Supports updating application status (e.g., accepted, rejected, pending)
 
- Saved Jobs (Bookmarking)
  Users can:
  - Save/unsave jobs
  - Retrieve a list of saved jobs
  - Data stored in a dedicated SavedJob table
  - Useful for users who want to revisit jobs later.
 
- AI Integration for Job Embeddings
Every job posting is converted into an embedding. Embeddings are stored in PostgreSQL (embedding_json) indexed into Elasticsearch for vector search
This enables your AI-driven job recommender system.
###  ResumeService
The Resume Service is a microservice in the SkillMatch platform that handles everything related to user resumes.
It allows users to upload resumes, extracts text from files, sends that text to the AI Service for analysis, and stores both the parsed data and the generated embedding vector.
These embeddings are later used to recommend the most relevant jobs to users.
#### ⚙️ Key Responsibilities
- Resume Upload & Text Extraction
  - Accepts resume files (PDF/DOCX) through REST API
  - Uses Apache Tika to extract readable text
  - Rebuilds full resume text when needed from parsed fields
- AI-Powered Resume Parsing
  Sends the extracted text to the AI Service (Python LLM, Phi-3) and receives structured data.
- Embedding Generation for Job Recommendations
  - Calls the AIClient to generate embedding vectors from the resume text
  - Saves these embeddings as JSON in the database
  - Provides data needed for vector-based job matching in the Job Service

- Resume Storage & CRUD Operations
    - Saves new resumes and updates existing ones
    - Retrieves resumes by user ID or resume ID
    - Provides an endpoint to return embedding vectors for recommendation logic
 
- Secure Communication & API Protection
    - All important endpoints require JWT authentication
    - A custom JwtAuthFilter validates tokens and extracts user roles
    - Uses Spring Security with stateless sessions
    - Enforces CORS rules for safe frontend communication

### AI-Service

The AI Service is a Python-based microservice that powers all AI-related functionality in the SkillMatch platform.
It provides endpoints for resume parsing using an LLM (Phi-3 via Ollama) and text embedding generation using SentenceTransformers.
The service transforms raw text into structured data and semantic vectors that are used by the Resume Service and Job Service for job matching and recommendations.

This service acts as the intelligence layer behind the entire platform.

#### ⚙️ Key Responsibilities
- Resume Parsing with LLMs
    - Receives extracted resume text from the Resume Service
    - Sends the text to the Phi-3 model through the Ollama API
    - Enforces structured, valid JSON output
    - Returns validated data using Pydantic models
This enables clean and consistent resume data for storing and matching.
- Embedding Generation for Semantic Search
  - Generates vector embeddings using SentenceTransformers (MiniLM-L6-v2)
  - Converts text into high-dimensional vectors
  - Used by Job Service to index job postings for vector search, perform KNN similarity search in Elasticsearch, and provide personalized job recommendations


## Setup & Installation Guide

This section explains everything you need to run SkillMatch development mode or inside Docker.
### For the spring boot microservices (user, resume, job)
#### Requirements
- Java 21+
- Maven
- PostgreSQL 15+
- Elasticsearch 8+

#### Set secret key
set your secret key in .env file

#### Configure Database in application-dev.yaml
Before running the services, update:
```bash
src/main/resources/application-dev.yaml
```

for example:
```bash
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/jobdb
    username: postgres
    password: your_password_here
```

#### Start Elasticsearch (Required by Job Service Only)
```bash
docker run -d \
  -p 9200:9200 \
  -e "discovery.type=single-node" \
  docker.elastic.co/elasticsearch/elasticsearch:8.12.0
```

#### Running Each Service
User Service

```bash
cd user-service
mvn spring-boot:run
```

```bash
cd resume-service
mvn spring-boot:run
```

```bash
cd job-service
mvn spring-boot:run
```

#### API Documentation (Swagger)

Each Spring Boot service provides automatic OpenAPI documentation:

- User Service → http://localhost:8081/swagger-ui.html
- Resume Service → http://localhost:8082/swagger-ui.html
- Job Service → http://localhost:8083/swagger-ui.html

### For the AI-Service (preferebaly in and environment)
#### Requirements
- Python 3.10+
- pip
- Ollama
- Phi-3 Model
Ollama download page:
👉 https://ollama.com/download

#### Pull the Phi-3 model

After installing Ollama:
```bash
ollama pull phi3
```
#### Install Python packages
```bash
pip install -r requirements.txt
```

#### Running the AI Service
#### Step 1 — Start Ollama (required)
```bash
ollama serve
```
#### Step 2 — Run the FastAPI Service

```bash
 uvicorn app.main:app --reload
```
#### Health Check
GET http://localhost:8000/health



### Frontend Setup (Vue.js 3 + Vite)
#### Requirements
- Node.js 18+
- npm

#### Install Dependencies

Inside the frontend/ directory, run:

```bash
npm install
```

#### Running the Frontend (Development Mode)

To start the dev server:
```bash
npm run dev
```
The frontend will start on:

http://localhost:5173


## 🐳 Docker Setup

The easiest and fastest way to run the entire SkillMatch platform is using Docker + Docker Compose.
Docker automatically spins up all microservices, connects them via internal networking, and provisions databases and supporting services like Elasticsearch.

### Requirements
- Docker
- Docker Compose
- Ollama
- Phi-3 model

The file docker-compose.yml contains:
- user-service
- resume-service
- job-service
- ai-service
- postgres
- elasticsearch
- frontend

### How to Run the Entire System
#### Build and run images
```bash
docker compose up --build
```
Check Running Containers

```bash
docker ps
```

#### Stop All Services


```bash
docker compose down
```
