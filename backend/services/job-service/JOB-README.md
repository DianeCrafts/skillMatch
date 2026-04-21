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
## ⚡ Performance Optimization (Indexing)

To improve query performance and scalability, database indexing was introduced based on real query patterns used by the Job Service.

### 🔍 Problem

Initial performance analysis using `EXPLAIN ANALYZE` revealed:

* Full table scans (`Seq Scan`) on the `jobs` table
* High number of rows filtered (up to ~10,000 rows)
* Unoptimized sorting on `created_at`
* Slower response times for filtered job queries and recruiter job listings

---

### 🛠️ Solution — Indexing Strategy

Indexes were added based on the most frequent query patterns:

```sql
CREATE INDEX idx_jobs_recruiter_created_at
ON jobs (recruiter_id, created_at DESC);

CREATE INDEX idx_saved_jobs_user_saved_at
ON saved_jobs (user_id, saved_at DESC);

CREATE INDEX idx_jobs_published_filters_created_at
ON jobs (status, location, employment_type, experience_level, created_at DESC);

CREATE INDEX idx_saved_jobs_user_job
ON saved_jobs (user_id, job_id);
```

---

### 📊 Results

#### 1. Filtered Job Search (`GET /api/jobs`)

* Before:

    * Sequential scan on entire table
    * ~10,000 rows filtered
    * Execution Time: ~1.45 ms
* After:

    * Bitmap Index Scan used
    * Reduced rows scanned significantly
    * Execution Time: ~0.25 ms

📷 Before

<!-- INSERT SCREENSHOT HERE -->

📷 After

<!-- INSERT SCREENSHOT HERE -->

---

#### 2. Keyword + Filter Search

* Before:

    * Full table scan
    * Expensive filtering with `LIKE`
    * Execution Time: ~1.79 ms
* After:

    * Index used for filtering
    * Reduced scanned rows
    * Execution Time: ~0.29 ms

📷 Before

<!-- INSERT SCREENSHOT HERE -->

📷 After

<!-- INSERT SCREENSHOT HERE -->

---

#### 3. Recruiter Jobs (`GET /api/jobs/me`)

* Before:

    * Sequential scan
    * ~9,500 rows filtered
    * Execution Time: ~1.13 ms
* After:

    * Index Scan on `(recruiter_id, created_at)`
    * Direct lookup
    * Execution Time: ~0.03 ms

📷 Before

<!-- INSERT SCREENSHOT HERE -->

📷 After

<!-- INSERT SCREENSHOT HERE -->

---

### 🚀 Impact

* Reduced query execution time by up to **~80–97%**
* Eliminated full table scans for critical endpoints
* Improved scalability for large datasets (10k+ jobs tested)
* Enabled efficient filtering, sorting, and pagination

---

### 🧠 Key Takeaways

* Indexing must align with **real query patterns**, not assumptions
* Composite indexes significantly improve multi-filter queries
* Sorting columns (e.g., `created_at DESC`) should be included in indexes
* Performance gains become more visible as dataset size grows

---

This optimization demonstrates practical backend performance tuning using PostgreSQL indexing and real query analysis.


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
