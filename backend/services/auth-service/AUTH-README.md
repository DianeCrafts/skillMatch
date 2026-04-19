# 🔐 SkillMatch – Auth Service

Authentication and authorization microservice for the **SkillMatch** platform.

This service is responsible for user registration, login, JWT token generation, and authentication of users across the system.

---

## 📌 Overview

The **Auth Service** is part of a microservices-based architecture and provides:

* User registration (Job Seeker / Recruiter)
* Secure login with email & password
* JWT-based authentication
* Role-based identity (for future authorization)
* Current authenticated user retrieval
* API documentation via Swagger/OpenAPI

---

## 🏗️ Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Security**
* **JWT (JJWT)**
* **PostgreSQL**
* **Maven**
* **Swagger / OpenAPI (Springdoc)**

---

## 📂 Project Structure

```
auth-service/
├── config/         # Security, Swagger, Password configs
├── controller/     # REST endpoints
├── dto/            # Request & Response objects
├── entity/         # JPA entities
├── enums/          # Role definitions
├── exception/      # Global exception handling
├── repository/     # Data access layer
├── security/       # JWT, filters, user details
├── service/        # Business logic
└── AuthServiceApplication.java
```

---

## 🔑 Features

### 1. User Registration

* Register with:

    * First name
    * Last name
    * Email (normalized to lowercase)
    * Password (hashed)
    * Role: `JOB_SEEKER` or `RECRUITER`
* Prevents duplicate email registration

---

### 2. Authentication (Login)

* Login using email + password
* Returns:

    * JWT token
    * User role
    * Basic user info

---

### 3. JWT Authentication

* Stateless authentication using JWT
* Token includes:

    * `userId`
    * `email`
    * `role`
* Used for securing endpoints across services

---

### 4. Current User Endpoint

* Retrieve currently authenticated user
* Requires valid Bearer token

---

### 5. Validation & Error Handling

* Input validation using annotations
* Global exception handling
* Standardized API response format

---

## 🔐 Roles

```text
JOB_SEEKER
RECRUITER
```

---

## 🌐 API Endpoints

| Method | Endpoint             | Description                    |
| ------ | -------------------- | ------------------------------ |
| POST   | `/api/auth/register` | Register new user              |
| POST   | `/api/auth/login`    | Login and get JWT              |
| GET    | `/api/auth/me`       | Get current authenticated user |

---

## 📥 Example Requests

### 🔹 Register

```json
{
  "firstName": "Sara",
  "lastName": "Ali",
  "email": "sara@example.com",
  "password": "StrongPass123",
  "role": "JOB_SEEKER"
}
```

---

### 🔹 Login

```json
{
  "email": "sara@example.com",
  "password": "StrongPass123"
}
```

---

### 🔹 Authorization Header

```
Authorization: Bearer <your-jwt-token>
```

---

## 📤 Example Response

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "sara@example.com",
    "role": "JOB_SEEKER",
    "firstName": "Sara",
    "lastName": "Ali"
  }
}
```

---

## ⚙️ Configuration

### application.yml

```yaml
server:
  port: 8081

app:
  api:
    base-path: /api/v1
  jwt:
    secret: ${JWT_SECRET}
    expiration: ${JWT_EXPIRATION:86400000}
```

---

## 🔧 Environment Variables

```text
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
AUTH_DB_NAME=skillmatch_auth

REDIS_HOST=localhost
REDIS_PORT=6379

JWT_SECRET=your_base64_secret
JWT_EXPIRATION=86400000
```

---

## 🗄️ Database

* PostgreSQL database per service
* Table: `users`
* Email is unique and normalized to lowercase

---

## 📊 Swagger / API Docs

Access Swagger UI:

```
http://localhost:8081/swagger-ui.html
```

OpenAPI JSON:

```
http://localhost:8081/v3/api-docs
```

---

## 🔒 Security

* Passwords are hashed using **BCrypt**
* Stateless session management
* JWT-based authentication
* Protected endpoints require Bearer token
* CSRF disabled for REST API

---

## ▶️ Running the Service

### 1. Start PostgreSQL

Make sure your DB is running.

### 2. Set environment variables

In IntelliJ or `.env`

### 3. Run the service

```bash
mvn spring-boot:run
```

---

## 🧪 Testing

You can test the service using:

* Swagger UI
* Postman
* curl

### Recommended flow:

1. Register user
2. Login
3. Copy JWT token
4. Call `/me` with Bearer token

---

## 🚀 Future Improvements (Optional)

* Refresh tokens
* Email verification
* Password reset
* Account locking
* Redis token blacklist
* OAuth2 / social login
* Gateway-level authentication

---

## 📌 Notes

* This service only handles **authentication**
* Profile, job data, and other logic belong to other microservices
* Designed to integrate with API Gateway and other services

---

## 👤 Author

SkillMatch Backend – Auth Service
