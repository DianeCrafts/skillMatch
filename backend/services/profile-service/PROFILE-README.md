# 📄 Profile Service — SkillMatch

## 📌 Overview

The **Profile Service** is a core microservice in the SkillMatch platform responsible for managing user profiles.

It allows authenticated users (primarily job seekers) to:

* Create and manage their personal profile
* Add skills, work experience, and education
* Maintain structured, extensible professional data

This service is designed to be **independent**, **scalable**, and **extensible** for future features such as recruiter/company profiles and resume metadata.

---

## 🏗️ Architecture Context

This service is part of a **microservices-based backend**:

```
backend/
├── api-gateway/
├── auth-service/
├── profile-service/   ← you are here
├── job-service/
├── application-service/
├── file-service/
└── notification-service/
```

### Key Characteristics

* **Spring Boot REST microservice**
* **PostgreSQL database (per service)**
* **JWT-based stateless authentication**
* **Validated via API Gateway + revalidated internally**
* **Swagger/OpenAPI for testing**

---

## 🎯 Responsibilities

The Profile Service is responsible for:

* Managing **user profile data**
* Enforcing **ownership rules** (users can only modify their own data)
* Structuring profile information into:

    * Profile core data
    * Skills
    * Work Experience
    * Education

---

## ⚙️ Tech Stack

* **Java 17+**
* **Spring Boot 3.x**
* **Spring Security (JWT Resource Server)**
* **Spring Data JPA (Hibernate)**
* **PostgreSQL**
* **Swagger / OpenAPI (springdoc)**
* **Maven**

---

## 🔐 Authentication & Security

* Uses **JWT tokens issued by auth-service**
* Token contains:

    * `userId`
    * `role`
    * `sub` (username/email)
* Profile Service:

    * **validates JWT signature**
    * extracts user identity
    * enforces ownership rules

### 🔑 Important Rule

> ❗ A user can ONLY access and modify their own profile.

---

## 📦 Project Structure

```
src/main/java/com/skillmatch/profile/
├── config/            # Security & OpenAPI configuration
├── controller/        # REST endpoints
├── dto/               # Request/Response models
│   ├── request/
│   ├── response/
│   └── auth/
├── entity/            # JPA entities
├── exception/         # Custom exceptions + handler
├── mapper/            # Entity → DTO mapping
├── repository/        # Data access layer
├── security/          # JWT user extraction
└── service/           # Business logic
```

---

## 🧩 Domain Model

### 👤 Profile

* One profile per user
* Linked by `userId` from JWT

### 🧠 Skills

* Simple list of skill names
* Case-insensitive uniqueness per profile

### 💼 Work Experience

* Job history entries
* Supports:

    * current job
    * sorting
    * detailed descriptions

### 🎓 Education

* Academic records
* Flexible structure for future expansion

---

## 🚀 API Endpoints

### 👤 Profile

| Method | Endpoint           | Description         |
| ------ | ------------------ | ------------------- |
| POST   | `/api/profiles/me` | Create profile      |
| GET    | `/api/profiles/me` | Get current profile |
| PUT    | `/api/profiles/me` | Update profile      |

---

### 🧠 Skills

| Method | Endpoint                            |
| ------ | ----------------------------------- |
| POST   | `/api/profiles/me/skills`           |
| PUT    | `/api/profiles/me/skills/{skillId}` |
| DELETE | `/api/profiles/me/skills/{skillId}` |

---

### 💼 Work Experience

| Method | Endpoint                            |
| ------ | ----------------------------------- |
| POST   | `/api/profiles/me/experiences`      |
| PUT    | `/api/profiles/me/experiences/{id}` |
| DELETE | `/api/profiles/me/experiences/{id}` |

---

### 🎓 Education

| Method | Endpoint                          |
| ------ | --------------------------------- |
| POST   | `/api/profiles/me/education`      |
| PUT    | `/api/profiles/me/education/{id}` |
| DELETE | `/api/profiles/me/education/{id}` |

---

## 🧪 Swagger / API Testing

Swagger UI is available at:

```
http://localhost:8082/swagger-ui.html
```

### 🔐 Authorization

Use:

```
Bearer <JWT_TOKEN>
```

---

## ⚙️ Configuration

### application.yml

Contains shared configuration:

* service name
* port
* swagger config

### application-dev.yml

Contains environment-specific config:

* PostgreSQL connection
* Redis (not yet used)
* Hibernate settings
* JWT secret

---

## 🌱 Features (v1)

### ✅ Implemented

* Profile creation & update
* Skill management
* Work experience management
* Education management
* JWT-based authentication
* Ownership validation
* Validation & error handling
* Swagger integration

---

### 🚧 Future Improvements

* Recruiter/company profiles
* Resume metadata & file integration
* Public profile view
* Profile search
* Profile completeness scoring
* Skills normalization system
* Caching with Redis
* Pagination & filtering

---

## 🧠 Design Principles

* **Single Responsibility** per service
* **Stateless authentication**
* **DTO-based API (no entity exposure)**
* **Strict ownership enforcement**
* **Extensible domain model**
* **Clean layered architecture**

---

## 🛠️ Running Locally

### 1. Start PostgreSQL

Ensure database exists:

```
skillmatch_profile
```

### 2. Set environment variables

```
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
PROFILE_DB_NAME=skillmatch_profile
JWT_SECRET=your_base64_secret
```

### 3. Run service

```
mvn spring-boot:run
```

---

## 🔗 Integration Flow

```
Client → API Gateway → Profile Service
                ↓
            JWT validated
                ↓
        Profile operations executed
```

---

## 📌 Notes

* This service does NOT handle authentication logic
* It relies entirely on **auth-service**
* JWT must be valid and signed with the same secret

---

## 👨‍💻 Author

Built as part of the **SkillMatch** microservices platform.

---
