# SkillMatch – AI-Powered Resume & Job Matching Platform

---

## 🔍 Abstract

### Problem
Job seekers waste time tailoring resumes and searching for suitable roles, while recruiters are overwhelmed with irrelevant applications.  
Traditional keyword matching fails to capture the *true meaning* behind skills and experience.

### Solution
**SkillMatch** uses AI and semantic search to:
-  Parse resumes automatically  
-  Extract and understand skills & experiences  
-  Match resumes to jobs *semantically*  
-  Provide personalized recommendations & skill-gap feedback  

---

##  System Architecture (High-Level)
<img width="700"  alt="diagram" src="https://github.com/user-attachments/assets/0e7442e9-1164-497b-9086-a3083c863beb" />

###  Layers Overview

| **Layer** | **Technology** | **Purpose** |
|------------|----------------|--------------|
| **Frontend** | Vue.js 3 + Vite | User & recruiter dashboards |
| **Backend Gateway** | Java 21 + Spring Boot | Main API orchestrator |
| **AI Layer** | Python + FastAPI + BERT | NLP semantic matching |
| **Databases & Search** | PostgreSQL, Elasticsearch, Redis | Data storage, semantic & keyword search |
| **File Storage** | MinIO / S3 | Resume and document storage |
| **Infrastructure** | Docker, Kubernetes | Deployment, scaling, CI/CD |
| **Monitoring** | Prometheus + Grafana | Health & performance monitoring |
| **Auth** | JWT + Spring Security | Secure authentication & authorization |

---

##  Key Microservices

###  UserService
- Manages user accounts, profiles, and authentication (JWT)
- Supports role-based access for job seekers & recruiters

###  JobService
- Handles job listings (CRUD)
- Stores metadata in PostgreSQL and indexes jobs in Elasticsearch

###  ResumeService
- Uploads, parses, and extracts resume text from PDF/DOCX
- Sends text to **AIService** for NLP processing

###  MatchService
- Fetches job & resume embeddings from **AIService**
- Computes cosine similarity for job matching
- Caches results in Redis for low latency

###  AIService (FastAPI)
- Uses **BERT** and **spaCy** for skill & experience extraction
- Embeds text and computes semantic similarity
- Provides skill gap analysis & recommendations

---

##  Technology Stack Summary

| **Layer** | **Tech** | **Purpose** |
|------------|-----------|-------------|
| Frontend | Vue.js + Vite | Single-page application |
| Backend | Java 21 + Spring Boot | Core API and orchestrator |
| AI | Python + FastAPI + BERT | NLP & semantic search |
| Storage | PostgreSQL, Elasticsearch, Redis | Data & search |
| Infrastructure | Docker, Kubernetes | CI/CD, scaling |
| Auth | JWT + Spring Security | Secure authentication |

---

##  Metrics & Validation

| **Metric** | **Target Goal** |
|-------------|-----------------|
| Match Accuracy | ≥ 85% precision/recall |
| Resume Parsing Success | ≥ 95% |
| Response Time | ≤ 500 ms (cached) |
| Scalability | 1000+ concurrent users |
| Test Coverage | > 80% |

---

## Repository Structure
```text
SkillMatch/
│
├── frontend/ # Vue.js 3 + Vite SPA
├── backend/ # Spring Boot gateway
├── services/
│ ├── ai/ # FastAPI (AIService)
│ ├── job/ # JobService
│ ├── resume/ # ResumeService
│ └── user/ # UserService
├── db/
│ ├── migrations/
│ └── schema.sql
├── infra/
│ ├── docker-compose.yml
│ ├── k8s/
│ └── ci-cd/
└── README.md
```

##  Development Plan

###  Step 1 – Setup Repository & Structure
- Create GitHub repo: **SkillMatch**
- Initialize folder structure  
- Add `.gitignore`, `Dockerfile`, and GitHub Actions CI pipeline

###  Step 2 – MVP Implementation
- Setup PostgreSQL schema  
- Build REST APIs (User + Job)  
- Implement Resume upload & AI matching endpoint  
- Integrate frontend dashboard  

###  Step 3 – Phase 2 Enhancements
- Add Elasticsearch integration  
- Add recruiter dashboard  
- Containerize services with Docker Compose  

###  Step 4 – Phase 3 Advanced Features
- Skill gap analysis  
- Notifications system  
- Analytics dashboard  
- Fine-tune BERT model for domain-specific accuracy  

---

##  Contributors

| **Role** | **Technology Stack** |
|-----------|-----------------------|
| **Backend & Infra** | Java 21, Spring Boot, Docker, Kubernetes |
| **AI & NLP** | Python, FastAPI, BERT, spaCy |
| **Frontend** | Vue.js 3, Vite |
| **Data Layer** | PostgreSQL, Elasticsearch, Redis, MinIO/S3 |
| **Monitoring** | Prometheus, Grafana |
| **CI/CD** | GitHub Actions |

---

##  Monitoring & Observability
- **Prometheus** – Metrics collection  
- **Grafana** – Dashboards and visualization  
- **Redis** – Cache optimization for quick response  
- **Kubernetes Probes** – Health checks and uptime monitoring  

---

##  Deployment & Infrastructure
- Fully containerized with **Docker**
- Deployable via **Kubernetes**
- CI/CD integrated using **GitHub Actions**
- Configuration management via `.env` & secrets
