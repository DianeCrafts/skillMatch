# Job Service — SkillMatch

## 📌 Overview

The **Job Service** is a core microservice in the SkillMatch platform responsible for managing job postings created by recruiters and enabling job discovery for job seekers.

It provides a REST API for:

* creating and managing job listings
* browsing and searching jobs
* saving jobs for later

This service is part of a **Spring Boot microservices architecture** and integrates with:

* Auth Service (JWT authentication)
* API Gateway (request routing and token forwarding)

---

## 🏗️ Architecture

### High-Level Flow

```
Client → API Gateway → Job Service → PostgreSQL
```

### Authentication Flow

1. User logs in via Auth Service
2. Auth Service returns JWT token
3. Client sends requests with:
   Authorization: Bearer <token>
4. API Gateway forwards request (with token)
5. Job Service:

    * extracts token from header
    * validates it using JwtService
    * extracts userId and role
    * applies authorization rules

---

## 🔐 Security Design

### Authentication

* JWT-based (stateless)
* Token issued by Auth Service
* Token verified inside Job Service (JJWT)

### Authorization (YES implemented)

* Only **RECRUITER** can:

    * create jobs
    * update jobs
    * delete jobs
    * publish/unpublish jobs
* Recruiters can only modify **their own jobs**
* Only **published jobs** are visible to general users

---

## 🚀 Features

### 👨‍💼 Recruiter Features

* Create job (default: DRAFT)
* Update own job
* Delete own job
* Publish job
* Unpublish job
* View own jobs (DRAFT + PUBLISHED)
* View own job details

---

### 🔍 Job Browsing (All Users)

* View published jobs
* View job details
* Search jobs (keyword)
* Filter jobs:

    * location
    * employment type
    * experience level
* Pagination support

---

### ⭐ Saved Jobs

* Save job
* Unsave job
* View saved jobs

---

## 📦 Tech Stack

* Java 17+
* Spring Boot
* Spring Data JPA
* PostgreSQL
* JJWT (JWT parsing)
* Lombok
* Swagger (OpenAPI)

---

## 📁 Project Structure

```
job-service/
├── controller/
│   ├── JobController.java
│   └── SavedJobController.java
├── service/
│   ├── JobService.java
│   ├── SavedJobService.java
│   └── impl/
├── entity/
│   ├── Job.java
│   ├── SavedJob.java
│   └── enums/
├── repository/
│   ├── JobRepository.java
│   └── SavedJobRepository.java
├── specification/
│   └── JobSpecification.java
├── security/
│   ├── JwtService.java
│   └── SecurityUtils.java
├── dto/
│   ├── request/
│   └── response/
├── exception/
│   └── GlobalExceptionHandler.java
├── config/
│   └── SecurityConfig.java
└── JobServiceApplication.java
```

---

## 🧠 Core Concepts

### 1. Job Lifecycle

```
DRAFT → PUBLISHED → (optional) DRAFT
```

* Jobs are created as **DRAFT**
* Only visible publicly when **PUBLISHED**

---

### 2. Ownership Model

* Each job has `recruiterId`
* Only owner can:

    * update
    * delete
    * publish/unpublish

---

### 3. Search & Filtering

Implemented using:

* **Spring Data JPA Specifications**

Filters:

* keyword (title + description)
* location
* employment type
* experience level

---

### 4. Saved Jobs

* Users can save published jobs
* Unique constraint: (userId, jobId)
* Stored in `saved_jobs` table

---

## 🔌 API Endpoints

### Job Management (Recruiter)

| Method | Endpoint                   | Description   |
| ------ | -------------------------- | ------------- |
| POST   | `/api/jobs`                | Create job    |
| PUT    | `/api/jobs/{id}`           | Update job    |
| DELETE | `/api/jobs/{id}`           | Delete job    |
| PATCH  | `/api/jobs/{id}/publish`   | Publish job   |
| PATCH  | `/api/jobs/{id}/unpublish` | Unpublish job |
| GET    | `/api/jobs/me`             | Get my jobs   |
| GET    | `/api/jobs/me/{id}`        | Get my job    |

---

### Job Browsing

| Method | Endpoint         | Description                      |
| ------ | ---------------- | -------------------------------- |
| GET    | `/api/jobs`      | List jobs (filters + pagination) |
| GET    | `/api/jobs/{id}` | Get job details                  |

---

### Saved Jobs

| Method | Endpoint              | Description    |
| ------ | --------------------- | -------------- |
| POST   | `/api/jobs/{id}/save` | Save job       |
| DELETE | `/api/jobs/{id}/save` | Unsave job     |
| GET    | `/api/jobs/saved`     | Get saved jobs |

---

## 🧪 Testing

Use:

* Swagger UI
* Postman

### Required Header

```
Authorization: Bearer <JWT>
```

### Test Flow

1. Login via Auth Service
2. Copy JWT
3. Call Job endpoints

---

## ⚠️ Known Limitations (v1)

* No caching (Redis not used yet)
* No job applications
* No company data integration
* N+1 query for saved jobs (to optimize later)
* Skills stored as `ElementCollection` (not normalized)

---

## 🚀 Future Improvements

* Redis caching
* Job application service
* Company integration (Profile Service)
* Skill normalization
* Search optimization (ElasticSearch)
* Event-driven architecture (Kafka)

---

## 🧑‍💻 Author

SkillMatch Project — Microservices-based Job Platform

---

## 📜 License

This project is for educational and development purposes.
