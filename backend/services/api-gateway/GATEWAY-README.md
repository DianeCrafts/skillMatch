# 🚀 SkillMatch – API Gateway Service

## 📌 Overview

The **API Gateway** is the central entry point for all client requests in the SkillMatch backend.

Instead of the frontend communicating directly with multiple microservices, it sends all requests to the gateway. The gateway is responsible for:

* Routing requests to the correct service
* Handling authentication at a high level
* Managing CORS for frontend access
* Providing a clean and unified API structure

This simplifies the frontend and keeps the architecture organized and scalable.

---

## 🏗️ Role in the Architecture

SkillMatch follows a **microservices architecture**, where each service is independent and owns its own data.

### Current Services

* `api-gateway` ✅ (this service)
* `auth-service`
* `profile-service` (planned)
* `job-service` (planned)
* `application-service` (planned)
* `file-service` (planned)
* `notification-service` (planned)

### Request Flow

```text
Frontend (Vue.js)
        ↓
   API Gateway (this service)
        ↓
  Target Microservice (auth, profile, etc.)
```

Example:

```text
POST /api/auth/login
→ Gateway
→ Auth Service
→ Response returned through Gateway
```

---

## 🎯 Purpose of the API Gateway

The gateway is designed to:

* Provide a **single entry point** for all backend communication
* Hide internal service structure from the frontend
* Standardize API routes (e.g., `/api/auth/**`, `/api/jobs/**`)
* Perform **basic authentication validation**
* Forward requests and responses transparently
* Prepare the system for future features like:

    * Logging
    * Rate limiting
    * Monitoring
    * Tracing

---

## 🔧 Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Cloud Gateway (WebFlux)**
* **Spring Security (JWT Resource Server)**
* **Maven**

---

## ⚙️ How It Works

### 1. Routing

Routes are defined in `application.yml`.

Example:

```yaml
routes:
  - id: auth-service
    uri: http://localhost:8081
    predicates:
      - Path=/api/auth/**
```

This means:

```text
/api/auth/** → forwarded to auth-service
```

---

### 2. Authentication (JWT)

The gateway uses **JWT-based authentication**.

#### Public endpoints:

* `POST /api/auth/register`
* `POST /api/auth/login`

#### Protected endpoints:

* All other `/api/**` routes

### Flow:

1. User logs in via gateway
2. `auth-service` returns JWT
3. Client sends JWT in headers:

   ```
   Authorization: Bearer <token>
   ```
4. Gateway:

    * Validates token (signature + expiration)
    * Forwards request if valid
5. Downstream service:

    * Validates token again
    * Uses claims for business logic

---

### 3. Header Forwarding

The gateway automatically forwards:

```http
Authorization: Bearer <token>
```

This allows each service to independently verify the user.

---

### 4. CORS Configuration

CORS is handled at the gateway level so the frontend can communicate easily.

Example:

```yaml
allowed-origins:
  - http://localhost:5173
```

---

## 🧩 API Structure

The gateway exposes a clean and consistent API:

```text
/api/auth/**
/api/profiles/**
/api/jobs/**
/api/applications/**
/api/files/**
/api/notifications/**
```

Even if some services are not implemented yet, routes are already prepared.

---

## 🔐 Security Design

The system uses **layered security**:

### Gateway Responsibilities

* Validate JWT presence
* Validate signature & expiration
* Block unauthorized requests early

### Service Responsibilities

* Validate JWT again
* Enforce business rules
* Handle authorization (roles, ownership)

This ensures **each service remains secure independently**.

---

## 📁 Project Structure

```text
api-gateway/
├── src/main/java/com/skillmatch/gateway
│   ├── ApiGatewayApplication.java
│   └── config/
│       ├── SecurityConfig.java
│       └── JwtConfig.java
│
├── src/main/resources/
│   ├── application.yml
│   └── application-dev.yml
│
└── pom.xml
```

---

## 🔑 Environment Variables

These must be configured when running the service:

```text
JWT_SECRET=your-secret-key

AUTH_SERVICE_URL=http://localhost:8081
PROFILE_SERVICE_URL=http://localhost:8082
JOB_SERVICE_URL=http://localhost:8083
APPLICATION_SERVICE_URL=http://localhost:8084
FILE_SERVICE_URL=http://localhost:8085
NOTIFICATION_SERVICE_URL=http://localhost:8086

FRONTEND_URL=http://localhost:5173
```

⚠️ The `JWT_SECRET` must match the one used in `auth-service`.

---

## 🧪 How to Test

### 1. Register

```http
POST http://localhost:8080/api/auth/register
```

### 2. Login

```http
POST http://localhost:8080/api/auth/login
```

### 3. Access protected endpoint

```http
GET http://localhost:8080/api/auth/me
Authorization: Bearer <token>
```

---

## 🚧 Current Status (Phase B2)

### ✅ Implemented

* Gateway routing
* JWT validation at gateway level
* Public vs protected route handling
* CORS configuration
* Integration with auth-service
* Environment-based configuration

### ⏳ Planned (Future Enhancements)

* Logging & tracing filters
* Rate limiting
* Circuit breakers
* Centralized error handling
* Distributed tracing (e.g., Zipkin)
* Service discovery (e.g., Eureka)

---

## 🧠 Design Principles

* Keep gateway **simple and focused**
* Avoid business logic in gateway
* Let services handle **authorization**
* Keep routes **predictable and consistent**
* Build **incrementally** (start simple → scale later)

---

## ✅ Summary

The API Gateway is the **foundation of communication** in SkillMatch.

It:

* Centralizes access to microservices
* Improves security and structure
* Simplifies frontend integration
* Prepares the system for scaling

This implementation represents a **clean, minimal, and production-aligned starting point** for a microservices gateway.

---
